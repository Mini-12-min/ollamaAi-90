package com.cuit.ai.bean;

import java.time.LocalDateTime;

public class ChatRecord {
    private String id;
    private String content;
    private String chat_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ChatRecord{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", chat_type='" + chat_type + '\'' +
                ", chat_time=" + chat_time +
                ", chat_member='" + chat_member + '\'' +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChat_type() {
        return chat_type;
    }

    public void setChat_type(String chat_type) {
        this.chat_type = chat_type;
    }

    public String getChat_member() {
        return chat_member;
    }

    public void setChat_member(String chat_member) {
        this.chat_member = chat_member;
    }

    public LocalDateTime getChat_time(LocalDateTime now) {
        return chat_time;
    }

    public void setChat_time(LocalDateTime chat_time) {
        this.chat_time = chat_time;
    }

    private LocalDateTime chat_time;
    private String chat_member;
}
