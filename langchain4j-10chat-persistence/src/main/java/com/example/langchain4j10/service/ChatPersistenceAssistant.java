package com.example.langchain4j10.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

public interface ChatPersistenceAssistant {

    String chat(@MemoryId Long userId, @UserMessage String message);
}
