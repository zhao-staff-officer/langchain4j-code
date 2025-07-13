package com.example.langchain07.service;

import reactor.core.publisher.Flux;

public interface ChatAssistant {

    String chat(String prompt);

    Flux<String> chatFlux(String prompt);
}
