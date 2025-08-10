package com.example.langchain4j13.config.store;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StoreConfig {

//    @Bean
//    public QdrantClient qdrantClient(){
//        QdrantGrpcClient.Builder grpcClientBuilder = QdrantGrpcClient.newBuilder("127.0.0.1",6334,false);
//        return new QdrantClient(grpcClientBuilder.build());
//    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore(){
        return QdrantEmbeddingStore.builder()
                .host("127.0.0.1")
                .port(6334)
                .collectionName("test-qdrant")
                .build();
    }
}
