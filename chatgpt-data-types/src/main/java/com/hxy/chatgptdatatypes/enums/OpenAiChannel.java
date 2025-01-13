package com.hxy.chatgptdatatypes.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OpenAiChannel {
    ChatGLM("ChatGLM"),
    ChatGPT("ChatGPT"),

    ;
    private final String code;

    public static OpenAiChannel getChannel(String model){
        if(model.toLowerCase().contains("gpt") || model.toLowerCase().contains("dall")) return OpenAiChannel.ChatGPT;
        if(model.toLowerCase().contains("glm")) return OpenAiChannel.ChatGLM;
        return null;
    }
}
