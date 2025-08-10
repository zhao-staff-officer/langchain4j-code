package com.example.langchain4j13.service;

import dev.langchain4j.service.Result;
import dev.langchain4j.service.TokenStream;

public interface ChatAssistant {

    TokenStream chat(String message);
}
