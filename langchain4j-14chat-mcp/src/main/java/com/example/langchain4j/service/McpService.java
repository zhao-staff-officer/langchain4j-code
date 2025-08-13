package com.example.langchain4j.service;

import reactor.core.publisher.Flux;

public interface McpService {

    Flux<String> chat(String question);
}
