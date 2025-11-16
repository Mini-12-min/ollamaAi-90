package com.cuit.ai.utils;

public enum SSEMsgType {
    MESSAGE ("message","单词发送普通消息"),
    ADD("add","消息追加用于stream流式推送"),
    FINISH("finish","消息完成"),
    CUSTOM_EVENT("custom_event","自定义消息"),
    DONE("done","消息完成");

    public final String type;
    public final String value;

    SSEMsgType(String type, String value) {
        this.type = type;
        this.value = value;
    }
}
