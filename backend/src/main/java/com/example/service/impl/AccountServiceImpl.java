package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Account;
import com.example.entity.dto.UploadFile;
import com.example.entity.vo.request.AccountAddVO;
import com.example.entity.vo.request.ConfirmResetVO;
import com.example.entity.vo.request.EmailRegisterVO;
import com.example.entity.vo.request.EmailResetVO;
import com.example.mapper.AccountMapper;
import com.example.service.AccountService;
import com.example.utils.Const;
import com.example.utils.FlowUtils;
import com.example.utils.UploadFileUtils;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Primary
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Resource
    FlowUtils flowUtils;
    @Resource
    UploadFileUtils uploadFileUtils;
    @Resource
    AmqpTemplate amqpTemplate;

    @Resource
    AccountMapper accountMapper;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = findByNameOrEmail(username);
        if(account == null)
            throw new UsernameNotFoundException("用户名或密码错误");
        return User
                .withUsername(username)
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    public Account findByNameOrEmail(String text) {
        return this.query()
                .eq("binary username", text).or()
                .eq("email", text)
                .one();
    }

    @Override
    public String registerEmailVerifyCode(String type, String email, String ip) {
        //加锁防止同一时间过多请求
        synchronized (ip.intern()) {
            if (!this.verifyLimit(ip)) {
                return "请求频繁，请稍后再试";
            }
            //生成验证码
            Random random = new Random();
            int code = random.nextInt(899999) + 100000;
            Map<String, Object> data  = Map.of("type", type, "email", email, "code", code);
            amqpTemplate.convertAndSend("mail", data);
            stringRedisTemplate.opsForValue()
                    .set(Const.VERIFY_EMAIL_DATA + email, String.valueOf(code), 3, TimeUnit.MINUTES);

            return null;
        }
    }

    @Override
    public String registerEmailAccount(EmailRegisterVO vo) {
        String email = vo.getEmail();
        String username = vo.getUsername();
        String key = Const.VERIFY_EMAIL_DATA + email;
        String code = stringRedisTemplate.opsForValue().get(key);
        if (code == null) return "请先获取验证码";
        if (!code.equals(vo.getCode())) return "验证码输入错误，请重新输入";
        if (this.existsAccountByEmail(email)) return "此电子邮箱已被注册！";
        if (this.existsAccountByUsername(username)) return "此用户名已被注册！";
        String password = encoder.encode(vo.getPassword());
        Account account = new Account(null, username, password, email, "user", new Date(), null, null, null);
        if (this.save(account)) {
            stringRedisTemplate.delete(key);
            return null;
        } else {
            return "内部错误，请联系管理员";
        }
    }

    @Override
    public String addAccount(AccountAddVO vo) {
        String email = vo.getEmail();
        String username = vo.getUsername();
        String role = vo.getRole();
        String password = encoder.encode(vo.getUsername());
        Account account = new Account(null, username, password, email, role, new Date(), null, null, null);
        if (this.save(account))
            return null;
        else
            return "内部错误，请联系管理员";
    }

    @Override
    public Account selectOneByEmail(String email) {
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        return accountMapper.selectOne(wrapper);
    }


    @Override
    public String resetConfirm(ConfirmResetVO vo) {
        String email = vo.getEmail();
        String code = stringRedisTemplate.opsForValue().get(Const.VERIFY_EMAIL_DATA + email);
        if(code == null) return "请先获取验证码";
        if(!code.equals(vo.getCode())) return "验证码错误，请重新输入";
        return null;
    }

    @Override
    public String resetEmailAccountPassword(EmailResetVO vo) {
        String email = vo.getEmail();
        String verify = this.resetConfirm(new ConfirmResetVO(vo.getEmail(), vo.getCode()));
        if(verify != null) return verify;
        String password = encoder.encode(vo.getPassword());
        boolean update = this.update().eq("email", email).set("password", password).update();
        if(update) {
            stringRedisTemplate.delete(Const.VERIFY_EMAIL_DATA + email);
        }
        return null;
    }

    @Override
    public String updateAvatar(MultipartFile file) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null){
            throw new RuntimeException("登录状态已过期");
        }
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Account account = findByNameOrEmail(userDetails.getUsername());
            UploadFile uploadFile = uploadFileUtils.upload(account.getUsername(), file.getOriginalFilename(), file);
            account.setAvatar(uploadFile.getType() + "/" + uploadFile.getFileName());
            boolean update = this.update().eq("email",account.getEmail()).set("avatar", account.getAvatar()).update();
            if(update)
                return null;
            else
                return "内部错误，请联系管理员";
        }
        throw new RuntimeException("找不到当前登录信息");
    }

    @Override
    public String updateUsername(String username) {
        Account account = getCurrentLoginUser();
        boolean update = this.update().eq("email", account.getEmail()).set("username",username).update();
        if (update)
            return null;
        else
            return "内部错误，请联系管理员";
    }

    @Override
    public IPage<Account> selectAccountPage(Page<Account> page, Wrapper<Account> wrapper) {
        return accountMapper.selectPage(page, wrapper);
    }

    @Override
    public IPage<Account> selectAccountByDetailPage(Page<Account> page, String detail) {
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        if (!"".equals(detail)) {
            wrapper.like("username", detail).or()
                    .like("email", detail).or()
                    .eq("role", detail);
        }
        return selectAccountPage(page, wrapper);
    }

    private boolean verifyLimit(String ip) {
        String key = Const.VERIFY_EMAIL_LIMIT + ip;
        return flowUtils.limitOnceCheck(key, 60);
    }

    private boolean existsAccountByEmail(String email) {
        return this.baseMapper.exists(Wrappers.<Account>query().eq("email", email));
    }

    private boolean existsAccountByUsername(String username) {
        return this.baseMapper.exists(Wrappers.<Account>query().eq("username", username));
    }

    //获取当前用户信息
    public Account getCurrentLoginUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null){
            throw new RuntimeException("登录状态已过期");
        }
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return findByNameOrEmail(userDetails.getUsername());
        }
        throw new RuntimeException("找不到当前登录信息");
    }
}
