package com.hxy.chatgptdatatypes.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatGPTModel {

    /** gpt-3.5-turbo */
    GPT_3_5_TURBO("gpt-3.5-turbo"),
    GLM_4("glm-4"),
    /** 文生图 */
    DALL_E_2("dall-e-2"),
    DALL_E_3("dall-e-3"),

    ;
    private final String code;

}