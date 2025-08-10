package com.example.langchain4j13.config.embedding;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddingConfig {

    @Bean
    public EmbeddingModel embeddingModel(){
        return OpenAiEmbeddingModel.builder()
                .apiKey(System.getenv("QWEN-API-KEY"))
                .modelName("text-embedding-v3")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();
    }

}
