package com.example.langchain4j13.config;

import com.example.langchain4j13.service.ChatAssistant;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.experimental.rag.content.retriever.sql.SqlDatabaseContentRetriever;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.aggregator.ContentAggregator;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.router.DefaultQueryRouter;
import dev.langchain4j.rag.query.router.LanguageModelQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

@Configuration
public class LLMConfig {



    @Bean
    public ChatAssistant assistant( ChatModel chatModel,
                                    StreamingChatModel streamingChatModel,
                                    EmbeddingStoreContentRetriever embeddingStoreContentRetriever,
                                    SqlDatabaseContentRetriever sqlDatabaseContentRetriever){
//        Map<ContentRetriever, String> retrieverToDescription = new HashMap<>();
//        retrieverToDescription.put(embeddingStoreContentRetriever, "Embedding Store");
//        retrieverToDescription.put(sqlDatabaseContentRetriever, "SQL Database");
//        QueryRouter queryRouter = new LanguageModelQueryRouter(chatModel,retrieverToDescription);
        QueryRouter queryRouter = new DefaultQueryRouter(embeddingStoreContentRetriever,sqlDatabaseContentRetriever);
        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder().queryRouter(queryRouter).build();

        return AiServices.builder(ChatAssistant.class)
                .chatModel(chatModel)
                .streamingChatModel(streamingChatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(50))
                .retrievalAugmentor(retrievalAugmentor)
//                .contentRetriever(embeddingStoreContentRetriever)
                .build();
    }
}
