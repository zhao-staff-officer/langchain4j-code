package com.example.langchain4j13.config.retriever;

import com.mysql.cj.jdbc.MysqlDataSource;
import dev.langchain4j.experimental.rag.content.retriever.sql.SqlDatabaseContentRetriever;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

@Configuration
public class RetrieverConfig {

    @Bean
    public EmbeddingStoreContentRetriever embeddingRetriever(EmbeddingModel embeddingModel, EmbeddingStore embeddingStore){
        return EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .maxResults(3) //返回条数
//                .dynamicMaxResults(query -> 3)
//                .minScore(0.75) //相似度阈值0-1
//                .dynamicMinScore(query -> 0.75)
//                .filter(metadataKey("name").isEqualTo("赵参谋")) //过滤器
//                .dynamicFilter(query -> {
//                    String userId = getUserId(query.metadata().chatMemoryId());
//                    return metadataKey("userId").isEqualTo(userId);
//                })
                .build();
    }

    @Value("${spring.datasource.url:jdbc:mysql://localhost:3306/your_database}")
    private String databaseUrl;

    @Value("${spring.datasource.username:root}")
    private String databaseUsername;

    @Value("${spring.datasource.password:password}")
    private String databasePassword;

    @Bean
    public SqlDatabaseContentRetriever sqlRetriever(ChatModel chatModel){
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(databaseUrl);
        dataSource.setUser(databaseUsername);
        dataSource.setPassword(databasePassword);

        return SqlDatabaseContentRetriever.builder()
                .dataSource(dataSource)
                .chatModel(chatModel)
                .build();
    }
}
