package com.hxy.chatgptdatadomain.openai.service.rule.impl;

import com.google.common.cache.Cache;
import com.hxy.chatgptdatadomain.openai.LogicStrategy;
import com.hxy.chatgptdatadomain.openai.model.aggregates.ChatProcessAggregate;
import com.hxy.chatgptdatadomain.openai.model.entity.RuleLogicEntity;
import com.hxy.chatgptdatadomain.openai.model.entity.UserAccountQuotaEntity;
import com.hxy.chatgptdatadomain.openai.model.valobj.LogicCheckTypeVO;
import com.hxy.chatgptdatadomain.openai.service.rule.ILogicFilter;
import com.hxy.chatgptdatadomain.openai.service.rule.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@LogicStrategy(logicModel = DefaultLogicFactory.LogicModel.ACCESS_LIMIT)
public class AccessLimitFilter implements ILogicFilter<UserAccountQuotaEntity> {

    @Value("${app.config.limit-count:10}")
    private Integer limitCount;

    @Value("${app.config.white-list}")
    private String whiteListStr;

    @Resource
    private Cache<String, Integer> visitCache;

    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess, UserAccountQuotaEntity data) throws Exception {
        // 1. 白名单用户直接放行
        if(chatProcess.isWhiteList(whiteListStr)){
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.SUCCESS)
                    .data(chatProcess).build();
        }
        String openid = chatProcess.getOpenid();
        // 2. 个人账户不为空，不做系统访问次数拦截
        if (null != data) {
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.SUCCESS).data(chatProcess).build();
        }

        // 2. 访问次数判断
        int visitCount = visitCache.get(openid, () -> 0);
        if (visitCount < limitCount) {
            visitCache.put(openid, visitCount + 1);
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.SUCCESS).data(chatProcess).build();
        }

        return RuleLogicEntity.<ChatProcessAggregate>builder()
                .info("您今日的免费" + limitCount + "次，已耗尽！")
                .type(LogicCheckTypeVO.REFUSE).data(chatProcess).build();

    }
}
