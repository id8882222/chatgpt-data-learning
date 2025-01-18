package com.hxy.chatgptdatadomain.openai.service.rule.impl;

import com.hxy.chatgptdatadomain.openai.LogicStrategy;
import com.hxy.chatgptdatadomain.openai.model.aggregates.ChatProcessAggregate;
import com.hxy.chatgptdatadomain.openai.model.entity.RuleLogicEntity;
import com.hxy.chatgptdatadomain.openai.model.entity.UserAccountQuotaEntity;
import com.hxy.chatgptdatadomain.openai.model.valobj.LogicCheckTypeVO;
import com.hxy.chatgptdatadomain.openai.service.rule.ILogicFilter;
import com.hxy.chatgptdatadomain.openai.service.rule.factory.DefaultLogicFactory;
import com.hxy.chatgptdatadomain.repository.IOpenAiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@LogicStrategy(logicModel =  DefaultLogicFactory.LogicModel.USER_QUOTA)
public class UserQuotaFilter implements ILogicFilter<UserAccountQuotaEntity> {

    @Resource
    private IOpenAiRepository openAiRepository;

    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess, UserAccountQuotaEntity data) throws Exception {
        if(data.getSurplusQuota() > 0){
            // 扣减账户额度；因为是个人账户数据，无资源竞争，所以直接使用数据库也可以。但为了效率，也可以优化为 Redis 扣减。
            int updateCount = openAiRepository.subAccountQuota(data.getOpenid());
            log.info("ChatGPTAIServiceController updateCount:{}", updateCount);
            if(0 != updateCount){
                return RuleLogicEntity.<ChatProcessAggregate>builder()
                        .type(LogicCheckTypeVO.SUCCESS).data(chatProcess).build();
            }
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .info("个人账户，总额度【" + data.getTotalQuota() + "】次，已耗尽！")
                    .type(LogicCheckTypeVO.REFUSE)
                    .data(chatProcess).build();
        }
        return RuleLogicEntity.<ChatProcessAggregate>builder()
                .info("个人账户，总额度【" + data.getTotalQuota() + "】次，已耗尽！")
                .type(LogicCheckTypeVO.REFUSE)
                .data(chatProcess).build();
    }
}
