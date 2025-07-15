package com.example.langchain4j08.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

public interface ChatMemoryAssistant {

    String chatWithMemory(@MemoryId Long userId, @UserMessage String prompt);
}
