package com.hxy.chatgptdatadomain.openai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hxy.chatgptdatadomain.openai.model.aggregates.ChatProcessAggregate;
import com.hxy.chatgptdatatypes.common.Constants;
import com.hxy.chatgptdatatypes.exception.ChatGPTException;
import com.hxy.chatgptsdkjavalearning.session.OpenAiSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;

@Slf4j
public abstract class AbstractChatService implements IChatService{

    @Resource
    protected OpenAiSession openAiSession;

    @Override
    public ResponseBodyEmitter completions(ResponseBodyEmitter emitter, ChatProcessAggregate chatProcess) {
        try{
            //1.请求应答
            // 当 ResponseBodyEmitter 完成（即 emitter.complete() 被调用）时触发emitter.onCompletion。
            emitter.onCompletion(() -> {
                log.info("流式问答请求完成，使用模型：{}", chatProcess.getModel());
            });
            emitter.onError(throwable -> {
                log.error("流式问答请求疫情，使用模型：{}", chatProcess.getModel(), throwable);
            });
            // TODO: 2025/1/12 2.账户获取
            // TODO: 2025/1/12 3.规则过滤
            // 4. 应答处理【chatGPT、chatGLM策略模式】
            this.doMessageResponse(chatProcess, emitter);
        }catch (Exception e){
            throw new ChatGPTException(Constants.ResponseCode.UN_ERROR.getCode(), Constants.ResponseCode.UN_ERROR.getInfo());
        }
        // 5. 返回结果
        return emitter;
    }
    protected abstract void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter responseBodyEmitter) throws JsonProcessingException;
}
