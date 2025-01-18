package com.hxy.chatgptdatadomain.repository;

import com.hxy.chatgptdatadomain.openai.model.entity.UserAccountQuotaEntity;

/**
 * OpenAi 仓储接口
 */
public interface IOpenAiRepository {
    int subAccountQuota(String openai);
    UserAccountQuotaEntity queryUserAccount(String openid);
}
