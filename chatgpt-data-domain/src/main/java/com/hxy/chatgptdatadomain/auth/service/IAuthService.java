package com.hxy.chatgptdatadomain.auth.service;

import com.hxy.chatgptdatadomain.auth.model.entity.AuthStateEntity;

/**
 * 鉴权验证服务接口
 */
public interface IAuthService {
    /**
     * 验证登录
     * @param code 验证码
     * @return
     */
    AuthStateEntity doLogin(String code);

    boolean checkToken(String token);

    String openid(String token);
}
