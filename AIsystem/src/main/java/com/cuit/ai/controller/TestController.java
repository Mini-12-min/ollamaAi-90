package com.cuit.ai.controller;

import com.cuit.ai.bean.ChatRecord;
import com.cuit.ai.bean.Chatntity;
import com.cuit.ai.service.ChatRecordService;
import com.cuit.ai.service.OllamaService;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import java.util.Map;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("ollama")
public class TestController {
    @Autowired
    private OllamaService ollamaService;
    @Autowired
    private ChatClient chatClient;
    @Autowired
    private ChatRecordService chatRecordService;


    @GetMapping("/ai/chat")
    public Object helloWord(String msg) {
        return ollamaService.aiOllama(msg);
    }

    @GetMapping("ai/steam1")
    public Flux<ChatResponse> alliOllamaStream1 (String msg) {
        return ollamaService.aiOllamaStream1(msg);
    }
    @GetMapping("/ai/steam2")
    public List<String>  alliOllamaStream2(String msg){
        return ollamaService.aiOllamaStream2(msg);
    }

    @PostMapping ("/ai/doctor/stream")
    public List<String>  alliOllamaDoctorStream(@RequestBody Chatntity chatntity) {
        System.out.println(chatntity);
        String currentUserName =chatntity.getCurrentUserName();
        String message = chatntity.getMessage();
        return ollamaService.doDoctorStream(currentUserName,message);
    }

    @GetMapping("/getRecords")
    public Object getRecords(String who) {
        return chatRecordService.getCharRecord(who);
    }

//    @GetMapping("/getRecords")
//    public Object getRecords(String who) {
//        List<ChatRecord> list = chatRecordService.getCharRecord(who);
//
//        Map<String, List<ChatRecord>> grouped = list.stream()
//                .collect(Collectors.groupingBy(ChatRecord::getChat_type));
//
//        return Map.of(
//                "userMessages", grouped.getOrDefault("user", List.of()),
//                "aiMessages", grouped.getOrDefault("ai", List.of())
//        );
//    }


}


