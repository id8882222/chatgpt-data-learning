package com.hxy.chatgptdatadomain.weixin.model.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息实体
 */
@Data
@NoArgsConstructor
public class MessageTextEntity {
    @XStreamAlias("MsgId")
    private String msgId;
    @XStreamAlias("ToUserName")
    private String toUserName;
    @XStreamAlias("FromUserName")
    private String fromUserName;
    @XStreamAlias("CreateTime")
    private String createTime;
    @XStreamAlias("MsgType")
    private String msgType;
    @XStreamAlias("Content")
    private String content;
    @XStreamAlias("Event")
    private String event;
    @XStreamAlias("EventKey")
    private String eventKey;
}
