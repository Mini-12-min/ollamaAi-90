package com.cuit.ai.service;

import com.cuit.ai.utils.ChatTypeEnums;
import com.cuit.ai.utils.SSEMsgType;
import com.cuit.ai.utils.SSEServer;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

import java.util.List;

@Service
public class OllamaServiceImpl implements OllamaService {
    @Autowired
    private OllamaChatClient ollamaChatClient;

    @Autowired
    private ChatRecordService chatRecordService;

    @Override
    public Object aiOllama(String msg) {
        return ollamaChatClient.call(msg);
    }

    @Override
    public Flux<ChatResponse> aiOllamaStream1(String msg) {
        Prompt prompt=new Prompt(new UserMessage(msg));
        //返回结果是流式响应
        Flux<ChatResponse> stream = ollamaChatClient.stream(prompt);
        return stream;
    }

    @Override
    public List<String> aiOllamaStream2(String msg) {
        Prompt prompt=new Prompt(new UserMessage(msg));
        //返回的结果就是流式的响应
        Flux<ChatResponse> streamResponse = ollamaChatClient.stream(prompt);
        List<String> list = streamResponse.toStream().map(chatResponse -> {
            String content = chatResponse.getResult().getOutput().getContent();
            System.out.println(content);
            return content;
        }).collect(Collectors.toList());
        return list;
    }
    @Override
    public List<String> doDoctorStream(String userName, String message) {

        chatRecordService.saveChatRecord(userName,message, ChatTypeEnums.user);
        Prompt prompt=new Prompt(new UserMessage(message));
        //返回的结果就是流式的响应
        Flux<ChatResponse> streamResponse = ollamaChatClient.stream(prompt);
        List<String> list = streamResponse.toStream().map(chatResponse -> {
            String content = chatResponse.getResult().getOutput().getContent();
            System.out.println(content);

            SSEServer.sendMessage(userName,content, SSEMsgType.ADD);
            return content;
        }).collect(Collectors.toList());

        SSEServer.sendMessage(userName,"over",SSEMsgType.FINISH);
        //保存ai回复的消息
        StringBuffer sb=new StringBuffer();
        for (String s : list) {
            sb.append(s);
        }

        chatRecordService.saveChatRecord(userName,sb.toString(),ChatTypeEnums.bot);
        return list;

    }
}
