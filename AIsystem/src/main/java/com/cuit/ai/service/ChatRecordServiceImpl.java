package com.cuit.ai.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cuit.ai.bean.ChatRecord;
import com.cuit.ai.mapper.ChatRecordMapper;
import com.cuit.ai.utils.ChatTypeEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatRecordServiceImpl implements ChatRecordService {

    @Autowired
    private ChatRecordMapper chatRecordMapper;

    @Override
    public void saveChatRecord(String userName, String message, ChatTypeEnums chatType) {
        ChatRecord chatRecord = new ChatRecord();
        chatRecord.setContent(message);
        chatRecord.setChat_type(chatType.type);   //  取出 String
        chatRecord.setChat_time(LocalDateTime.now());
        chatRecord.setChat_member(userName);
        chatRecordMapper.insert(chatRecord);
    }

    @Override
    public List<ChatRecord> getCharRecord(String userName) {
        QueryWrapper<ChatRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chat_member", userName);
        return chatRecordMapper.selectList(queryWrapper);
    }

}