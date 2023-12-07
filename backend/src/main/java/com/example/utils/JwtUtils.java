package com.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {
    @Value("${spring.security.jwt.key}")
    String key;

    @Value("${spring.security.jwt.expire}")
    int expire;

    @Resource
    StringRedisTemplate template;
    //返回是否失效
    public boolean invalidJwt(String headerToken) {
        //无效token
        String token = this.convertToken(headerToken);
        if (token == null) return false;
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            DecodedJWT jwt = jwtVerifier.verify(token);
            String id = jwt.getId();
            return deleteToken(id, jwt.getExpiresAt());
        } catch (JWTVerificationException e) {
            return false;
        }
    }
    //拉黑token
    private boolean deleteToken(String uuid, Date time) {
        if(this.isInvalidToken(uuid))
            return false;
        Date now = new Date();
        //判断是否过期
        long expire = Math.max(time.getTime() - now.getTime(), 0);
        //存入Redis做到拉黑,后面参数指毫秒
        template.opsForValue().set(Const.JWT_BLACK_LIST + uuid, "", expire, TimeUnit.MILLISECONDS);
        return true;
    }

    //令牌是否失效
    private boolean isInvalidToken(String uuid) {
        return Boolean.TRUE.equals(template.hasKey(Const.JWT_BLACK_LIST + uuid));
    }

    public String createJwt(UserDetails details, int id, String email) {
        Algorithm algorithm = Algorithm.HMAC256(key);
        Date expire = this.expireTime();
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("id", id)
                .withClaim("email", email)
                //将用户的权限列表作为声明添加到JWT中。这里使用`details.getAuthorities()`获取用户的权限信息，然后将权限名称提取出来并转换为列表。
                .withClaim("authorities", details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                //过期时间
                .withExpiresAt(expire)
                //token颁发时间
                .withIssuedAt(new Date())
                //最后用算法签名
                .sign(algorithm);
    }

    //计算过期的目标时间
    public Date expireTime() {
        Calendar calendar = Calendar.getInstance();        //获取当前时间
        calendar.add(Calendar.HOUR, expire * 24);   //加上expire * 24 小时后为过期时间
        return  calendar.getTime();
    }

    public DecodedJWT resolveJwt(String headerToken) {
        String token = convertToken(headerToken);
        if(token == null) return  null;
        Algorithm algorithm = Algorithm.HMAC256(key);
        //验证JWT合法性，有无被篡改等
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            //验证为运行时异常需要手动捕获并处理
            DecodedJWT verifyJwt = jwtVerifier.verify(token);
            //是否被拉黑
            if(this.isInvalidToken(verifyJwt.getId()))
                return null;
            //判断令牌是否过期
            Date expireAt = verifyJwt.getExpiresAt();
            return new Date().after(expireAt) ? null : verifyJwt;
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    //加工token
    private String convertToken(String headerToken) {
        if(headerToken == null || !headerToken.startsWith("Bearer "))
            return null;
        return headerToken.substring(7);
    }

    //封装解析用户的方法
    public UserDetails toUser(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();
        return User
                .withUsername(claims.get("email").asString())
                .password("*****")
                .authorities(claims.get("authorities").asArray(String.class))
                .build();
    }
    //封装解析Id的方法
    public Integer toId(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();
        return claims.get("id").asInt();
    }
}
