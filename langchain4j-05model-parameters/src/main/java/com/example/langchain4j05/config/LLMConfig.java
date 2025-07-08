package com.example.langchain4j05.config;

import com.example.langchain4j05.listener.TestChatModelListener;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LLMConfig {

    @Bean(name = "qwen")
    public ChatModel chatModelQwen(){
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("QWEN-API-KEY"))
                .modelName("qwen-plus")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .logRequests(Boolean.TRUE)
                .logResponses(Boolean.TRUE)
                .listeners(List.of(new TestChatModelListener()))
                .build();
    }

    @Bean(name = "deepseek")
    public ChatModel chatModelDeepSeek(){
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("DEEPSEEK-API-KEY"))
                .baseUrl("https://api.deepseek.com/v1")
                .modelName("deepseek-chat")
                .build();
    }
}
