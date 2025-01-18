package com.hxy.chatgptdatadomain.openai.service;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.hxy.chatgptdatadomain.openai.model.aggregates.ChatProcessAggregate;


import com.hxy.chatgptdatadomain.openai.model.entity.RuleLogicEntity;
import com.hxy.chatgptdatadomain.openai.model.entity.UserAccountQuotaEntity;
import com.hxy.chatgptdatadomain.openai.model.valobj.LogicCheckTypeVO;
import com.hxy.chatgptdatadomain.openai.service.rule.ILogicFilter;
import com.hxy.chatgptdatadomain.openai.service.rule.factory.DefaultLogicFactory;
import com.hxy.chatgptsdkjavalearning.common.Constants;
import com.hxy.chatgptsdkjavalearning.domain.chat.ChatChoice;
import com.hxy.chatgptsdkjavalearning.domain.chat.ChatCompletionRequest;
import com.hxy.chatgptsdkjavalearning.domain.chat.ChatCompletionResponse;
import com.hxy.chatgptsdkjavalearning.domain.chat.Message;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.digester.Rule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
@Service
public class ChatService extends AbstractChatService{

    @Resource
    private DefaultLogicFactory logicFactory;

    @Override
    protected void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) throws JsonProcessingException {
        // 1.请求信息
        List<Message> messages = chatProcess.getMessages().stream()
                .map(entity -> Message.builder()
                        .role(Constants.Role.valueOf(entity.getRole().toUpperCase()))
                        .content(entity.getContent())
                        .name(entity.getName())
                        .build())
                .collect(Collectors.toList());

        // 2.封装参数
        ChatCompletionRequest chatCompletion  = ChatCompletionRequest.builder()
                .stream(true)
                .messages(messages)
                .model(ChatCompletionRequest.Model.GPT_3_5_TURBO.getCode())
                .build();
        // 3.请求应答
        openAiSession.chatCompletions(chatCompletion, new EventSourceListener() {
            @Override
            public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
                ChatCompletionResponse chatCompletionResponse = JSON.parseObject(data, ChatCompletionResponse.class);
                List<ChatChoice> choices = chatCompletionResponse.getChoices();
                for(ChatChoice chatChoice : choices){
                    Message delta  = chatChoice.getDelta();
                    if(Constants.Role.ASSISTANT.getCode().equals(delta.getRole())) continue;

                    //应答完成
                    String finishReason = chatChoice.getFinishReason();
                    if(StringUtils.isNoneBlank(finishReason) && "stop".equals(finishReason)){
                        emitter.complete();
                        break;
                    }

                    //发送消息
                    try {
//                        log.info("Sending data: {}", delta.getContent());
                        emitter.send(delta.getContent());
                    }catch (Exception e){
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    @Override
    protected RuleLogicEntity<ChatProcessAggregate> doCheckLogic(ChatProcessAggregate chatProcess, UserAccountQuotaEntity userAccountQuotaEntity, String... logics) throws Exception {
        Map<String, ILogicFilter<UserAccountQuotaEntity>> logicFilterMap = logicFactory.openLogicFilter();
        RuleLogicEntity<ChatProcessAggregate> entity = null;
        log.info("ChatService doCheckLogic logics", Arrays.toString(logics));
        for(String code : logics){
            if(DefaultLogicFactory.LogicModel.NULL.getCode().equals(code)) continue;
            entity = logicFilterMap.get(code).filter(chatProcess, userAccountQuotaEntity);
            if(!LogicCheckTypeVO.SUCCESS.equals(entity.getType())) return entity;
        }
        return entity != null ? entity : RuleLogicEntity.<ChatProcessAggregate>builder()
                .type(LogicCheckTypeVO.SUCCESS)
                .data(chatProcess).build();

    }
}
