package com.hxy.chatgptdatadomain.openai.service.rule;

import com.hxy.chatgptdatadomain.openai.model.aggregates.ChatProcessAggregate;
import com.hxy.chatgptdatadomain.openai.model.entity.RuleLogicEntity;
import com.hxy.chatgptdatadomain.openai.model.valobj.LogicCheckTypeVO;

/**
 * 规则过滤接口
 */
public interface ILogicFilter<T> {
    RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess, T data) throws  Exception;
}
