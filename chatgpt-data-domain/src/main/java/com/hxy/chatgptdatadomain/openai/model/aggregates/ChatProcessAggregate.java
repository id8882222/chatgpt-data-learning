package com.hxy.chatgptdatadomain.openai.model.aggregates;

import com.hxy.chatgptdatadomain.openai.model.entity.MessageEntity;
import com.hxy.chatgptdatatypes.enums.ChatGPTModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatProcessAggregate {
    /** 用户ID */
    private String token;
    /** 默认模型 */
    private String model = ChatGPTModel.GPT_3_5_TURBO.getCode();
    /** 问题描述 */
    private List<MessageEntity> messages;
}
