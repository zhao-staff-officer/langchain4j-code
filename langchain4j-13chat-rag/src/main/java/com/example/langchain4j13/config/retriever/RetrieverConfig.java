package com.example.langchain4j13.config.retriever;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

@Configuration
public class RetrieverConfig {

    @Bean
    public EmbeddingStoreContentRetriever embedingRetriever(EmbeddingModel embeddingModel, EmbeddingStore embeddingStore){
        return EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .maxResults(3) //返回条数
//                .dynamicMaxResults(query -> 3)
                .minScore(0.75) //相似度阈值0-1
//                .dynamicMinScore(query -> 0.75)
                .filter(metadataKey("name").isEqualTo("赵参谋")) //过滤器
//                .dynamicFilter(query -> {
//                    String userId = getUserId(query.metadata().chatMemoryId());
//                    return metadataKey("userId").isEqualTo(userId);
//                })
                .build();
    }
}
