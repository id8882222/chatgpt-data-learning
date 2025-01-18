package com.hxy.chatgptdatadomain.openai.service.rule.impl;

import com.hxy.chatgptdatadomain.openai.LogicStrategy;
import com.hxy.chatgptdatadomain.openai.model.aggregates.ChatProcessAggregate;
import com.hxy.chatgptdatadomain.openai.model.entity.RuleLogicEntity;
import com.hxy.chatgptdatadomain.openai.model.entity.UserAccountQuotaEntity;
import com.hxy.chatgptdatadomain.openai.model.valobj.LogicCheckTypeVO;
import com.hxy.chatgptdatadomain.openai.model.valobj.UserAccountStatusVO;
import com.hxy.chatgptdatadomain.openai.service.rule.ILogicFilter;
import com.hxy.chatgptdatadomain.openai.service.rule.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@LogicStrategy(logicModel = DefaultLogicFactory.LogicModel.ACCOUNT_STATUS)
public class AccountStatusFilter implements ILogicFilter<UserAccountQuotaEntity> {

    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess, UserAccountQuotaEntity data) throws Exception {
        // 账户可用，直接放行
        if(UserAccountStatusVO.AVAILABLE.equals(data.getUserAccountStatusVO())){
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.SUCCESS).build();
        }
        return RuleLogicEntity.<ChatProcessAggregate>builder()
                .info("您的账户已冻结，暂时不可使用。如果有疑问，可以联系客户解冻账户。")
                .type(LogicCheckTypeVO.REFUSE).build();
    }
}
