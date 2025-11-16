package com.cuit.ai.service;

import org.springframework.ai.chat.ChatResponse;
import reactor.core.publisher.Flux;

import java.util.List;

public interface OllamaService {

    Object aiOllama(String msg);

    Flux<ChatResponse> aiOllamaStream1(String prompt);

    List<String> aiOllamaStream2(String msg);

    List<String> doDoctorStream(String userName,String message);
}
