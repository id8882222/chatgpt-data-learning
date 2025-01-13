package com.hxy.chatgptdatatypes.exception;

public class ChatGPTException extends RuntimeException{
    /**
     * 异常码
     */
    private String code;

    /**
     * 异常信息
     */
    private String message;

    public ChatGPTException(String code) {
        this.code = code;
    }

    public ChatGPTException(String code, Throwable cause){
        this.code = code;
        super.initCause(cause);
    }

    public ChatGPTException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ChatGPTException(Throwable cause, String code, String message) {
        super.initCause(cause);
        this.code = code;
        this.message = message;
    }
}
