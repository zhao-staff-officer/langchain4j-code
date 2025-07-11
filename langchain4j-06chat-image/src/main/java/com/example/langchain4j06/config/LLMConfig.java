package com.example.langchain4j06.config;

import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LLMConfig {

    @Bean(name = "qwen")
    public ChatModel chatModelQwen(){
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("QWEN-API-KEY"))
                .modelName("qwen-vl-max")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();
    }

    @Bean
    public WanxImageModel wanxImageModel(){
        return WanxImageModel.builder()
                .apiKey(System.getenv("QWEN-API-KEY"))
                .modelName("wanx2.1-t2i-turbo")
                .build();
    }
}
