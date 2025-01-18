package com.hxy.chatgptdatadomain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.cache.Cache;
import com.hxy.chatgptdatadomain.auth.model.entity.AuthStateEntity;
import com.hxy.chatgptdatadomain.auth.model.valobj.AuthTypeVO;
import com.hxy.chatgptdatadomain.openai.model.aggregates.ChatProcessAggregate;
import com.hxy.chatgptdatadomain.openai.service.AbstractChatService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;

/**
 * 鉴权服务
 */
@Slf4j
@Service
public class AuthService extends AbstractAuthService {

    @Resource
    private Cache<String, String> codeCache;

    @Override
    protected AuthStateEntity checkCode(String code) {
        // 获取验证码校验
        String openId = codeCache.getIfPresent(code);
        if(StringUtils.isBlank(openId)){
            log.info("鉴权，用户输入的验证码不存在 {}", code);
            return AuthStateEntity.builder()
                    .code(AuthTypeVO.A0001.getCode())
                    .info(AuthTypeVO.A0001.getInfo())
                    .build();
        }
        // 移除缓存Key值
        codeCache.invalidate(openId);
        codeCache.invalidate(code);
        // 验证码校验成功
        return AuthStateEntity
                .builder()
                .code(AuthTypeVO.A0000.getCode())
                .info(AuthTypeVO.A0000.getInfo())
                .openId(openId)
                .build();
    }


    @Override
    public boolean checkToken(String token) {
        return isVerify(token);
    }

    @Override
    public String openid(String token) {
        Claims claims = decode(token);
        for (String key : claims.keySet()) {
            log.info("claims key:{}, value:{}", key, claims.get(key));
        }
        return claims.get("openId").toString();
    }


}
