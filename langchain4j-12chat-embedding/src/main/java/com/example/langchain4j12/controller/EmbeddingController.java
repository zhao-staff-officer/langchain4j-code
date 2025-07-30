package com.example.langchain4j12.controller;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

@Slf4j
@RestController
public class EmbeddingController {

    @Resource
    private EmbeddingModel embeddingModel;

    @Resource
    private QdrantClient qdrantClient;

    @Resource
    private EmbeddingStore<TextSegment> embeddingStore;

    @GetMapping("/embedding/embed")
    public String embed(){
        String prompt = """
                咏鸡
                你好呀
                """;
        Response<Embedding> embeddingResponse =embeddingModel.embed(prompt);
        System.out.println(embeddingResponse);
        return embeddingResponse.content().toString();
    }

    /**
     * 创建数据库实例和创建索引
     */
    @GetMapping("/embedding/createCollection")
    public void createCollection(){
        Collections.VectorParams build = Collections.VectorParams.newBuilder()
                .setDistance(Collections.Distance.Cosine)
                .setSize(1024)
                .build();
       qdrantClient.createCollectionAsync("test-qdrant",build);
    }

    @GetMapping("/embedding/add")
    public String add(){
        String prompt = """
                咏鸡
                你好呀
                """;
        TextSegment segment1 = TextSegment.from(prompt);
        segment1.metadata().put("author","zzyy");
        Embedding embedding1=embeddingModel.embed(segment1).content();
        String result = embeddingStore.add(embedding1,segment1);
        return  result;
    }

    @GetMapping("/embedding/query")
    public void query1(){
        Embedding queryEmbedding = embeddingModel.embed("咏鸡说的是什么").content();
        EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(1)
                .build();
        EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(embeddingSearchRequest);
        System.out.println(searchResult.matches().get(0).embedded().text());
    }

    @GetMapping("/embedding/query2")
    public void query2(){
        Embedding queryEmbedding = embeddingModel.embed("咏鸡").content();
        EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .filter(metadataKey("author").isEqualTo("zzyy"))
                .maxResults(1)
                .build();

        EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(embeddingSearchRequest);

        System.out.println(searchResult.matches().get(0).embedded().text());
    }

}
