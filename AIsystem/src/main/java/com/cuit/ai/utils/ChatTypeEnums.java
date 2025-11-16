package com.cuit.ai.utils;

public enum ChatTypeEnums {
    user ("user","用户发送的内容"),
    bot("ai","ai回复的消息");

    public final String type;
    public final String value;

    ChatTypeEnums(String type, String value) {
        this.type = type;
        this.value = value;
    }
}
