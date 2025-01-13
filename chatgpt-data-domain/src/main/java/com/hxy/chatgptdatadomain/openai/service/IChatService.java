package com.hxy.chatgptdatadomain.openai.service;

import com.hxy.chatgptdatadomain.openai.model.aggregates.ChatProcessAggregate;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

/**
 * OpenAi 应答请求
 */
public interface IChatService {
    ResponseBodyEmitter completions(ResponseBodyEmitter emitter, ChatProcessAggregate chatProcessAggregate);
}
