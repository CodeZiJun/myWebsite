package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Account;
import com.example.entity.dto.Archives;
import com.example.entity.vo.request.ArchivesAddVO;
import com.example.entity.vo.response.ArchivesVO;
import com.example.mapper.ArchivesMapper;
import com.example.service.AccountService;
import com.example.service.ArchivesService;
import com.github.yulichang.query.MPJQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ArchivesServiceImpl extends ServiceImpl<ArchivesMapper, Archives> implements ArchivesService {
    @Resource
    AccountService accountService;
    @Resource
    ArchivesMapper mapper;
    @Override
    public ArchivesVO selectDetailArchivesByEmail(String email) {
        Account account = accountService.selectOneByEmail(email);
        if (account != null) {
            return mapper.selectJoinOne(ArchivesVO.class,
                    new MPJQueryWrapper<Archives>()
                            .selectAll(Archives.class)
                            .eq("t.id", account.getArchivesId())
                            .select("dep.department_name")
                            .select("pos.position_name")
                            .leftJoin("db_department dep on t.department_id = dep.id")
                            .leftJoin("db_position pos on t.position_id = pos.id")
            );
        }
        else
            return null;
    }

    @Override
    public String addArchives(ArchivesAddVO vo) {
        Archives archives = new Archives();
        archives.setRealName(vo.getRealName());
        archives.setTelephone(vo.getTelephone());
        archives.setGender(vo.getGender());
        archives.setPersonalId(vo.getPersonalId());
        archives.setAddress(vo.getAddress());
        archives.setEducation(vo.getEducation());
        archives.setPersonalIntro(vo.getPersonalIntro());
        archives.setRenewTime(new Date());
        boolean flag = this.save(archives);
        Integer id = archives.getId();
        accountService.update().eq("email", vo.getEmail()).set("archives_id", id).update();
        if (flag)
            return null;
        else
            return "服务器内部错误，请联系管理员";
    }
}

