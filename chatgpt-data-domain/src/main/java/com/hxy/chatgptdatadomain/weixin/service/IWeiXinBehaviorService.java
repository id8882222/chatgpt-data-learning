package com.hxy.chatgptdatadomain.weixin.service;

import com.hxy.chatgptdatadomain.auth.model.entity.UserBehaviorMessageEntity;

/**
 * 受理用户行为接口
 */
public interface IWeiXinBehaviorService {
    String acceptUserBehavior(UserBehaviorMessageEntity userBehaviorMessageEntity);
}
