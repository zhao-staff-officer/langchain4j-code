package com.example.langchain4j13.config.chatmodel;

import com.example.langchain4j13.config.TestChatModelListener;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class ChatModelConfig {

    @Bean
    public ChatModel chatModel(){
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("QWEN-API-KEY"))
                .modelName("qwen-plus")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .logRequests(true)
                .logResponses(true)
                .listeners(List.of(new TestChatModelListener()))
                .build();
    }

    @Bean
    public StreamingChatModel streamingChatModel(){
        return OpenAiStreamingChatModel.builder()
                .apiKey(System.getenv("QWEN-API-KEY"))
                .modelName("qwen-plus")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .logRequests(true)
                .logResponses(true)
                .listeners(List.of(new TestChatModelListener()))
                .build();
    }
}
