package com.hxy.chatgptdatadomain.openai.model.entity;

import com.hxy.chatgptdatadomain.openai.model.valobj.LogicCheckTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 规则校验结果实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleLogicEntity<T> {
    private LogicCheckTypeVO type;
    private String info;
    private T data;
}
