package com.cuit.ai.service;

import com.cuit.ai.bean.ChatRecord;
import com.cuit.ai.utils.ChatTypeEnums;

import java.util.List;

public interface ChatRecordService {
    /*
    保存和ai的对话
    userName 用户名
    message 消息
    chatType 会话类型：user表示用户消息 bot 表示ai生成的消息
     */

    void saveChatRecord(String userName, String message, ChatTypeEnums chatType);
    //查询用户和ai的聊天信息
    List<ChatRecord> getCharRecord(String userName);
}
