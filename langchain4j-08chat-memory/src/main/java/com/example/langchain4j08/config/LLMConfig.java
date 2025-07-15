package com.example.langchain4j08.config;

import com.example.langchain4j08.service.ChatAssistant;
import com.example.langchain4j08.service.ChatMemoryAssistant;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.TokenCountEstimator;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiTokenCountEstimator;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LLMConfig {

    @Bean
    public ChatModel chatModelQwen(){
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("QWEN-API-KEY"))
                .modelName("qwen-long")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();
    }


    @Bean(name = "chat")
    public ChatAssistant chatAssistant(ChatModel chatModel){
        return AiServices.create(ChatAssistant.class,chatModel);
    }


    @Bean(name = "chatMessageWindowChatMemory")
    public ChatMemoryAssistant chatMemoryWindowChatMemory(ChatModel chatModel){
        return AiServices
                .builder(ChatMemoryAssistant.class)
                .chatModel(chatModel)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(100))
                .build();
    }

    @Bean(name = "chatTokenChatMemory")
    public ChatMemoryAssistant chatMemoryTokenChatMemory(ChatModel chatModel){
        TokenCountEstimator tokenCountEstimator = new OpenAiTokenCountEstimator("gpt-4");

        return AiServices
                .builder(ChatMemoryAssistant.class)
                .chatModel(chatModel)
                .chatMemoryProvider(memoryId -> TokenWindowChatMemory.withMaxTokens(1000,tokenCountEstimator))
                .build();
    }





}
