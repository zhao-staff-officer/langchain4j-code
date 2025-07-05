package com.example.langchain4j03.service;

import dev.langchain4j.service.spring.AiService;

@AiService
public interface ChatAssistant {

    String chat(String prompt);
}
