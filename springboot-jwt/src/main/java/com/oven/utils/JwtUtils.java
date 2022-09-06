package com.oven.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * JwtUtils
 */
@Slf4j
public final class JwtUtils {

    /**
     * 加密的secret
     */
    private static final String secret = "OvenSecret";

    /**
     * 生成token
     */
    public static String generateToken(Map<String, Object> map) {
        if (Objects.isNull(map)) {
            map = new HashMap<>();
        }
        long expire = 15 * 60;
        Date expireDate = new Date(System.currentTimeMillis() + expire * 1000); // 过期时间
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")   // 设置头部信息
                .setClaims(map)                 // 装入自定义的用户信息
                .setExpiration(expireDate)      // token过期时间
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 校验token并解析token
     *
     * @return Claims：它继承了Map,而且里面存放了生成token时放入的用户信息
     */
    public static Claims verifyAndGetClaimsByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getHeaderKey() {
        return "token";
    }

}