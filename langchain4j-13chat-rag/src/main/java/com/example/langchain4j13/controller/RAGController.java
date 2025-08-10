package com.example.langchain4j13.controller;

import com.example.langchain4j13.service.ChatAssistant;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiTokenCountEstimator;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.IngestionResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;


@Slf4j
@RestController
public class RAGController {

    @Resource
    private EmbeddingModel embeddingModel;

    @Resource
    private EmbeddingStore<TextSegment> embeddingStore;

    @Resource
    private ChatAssistant chatAssistant;

    @GetMapping("/rag/test")
    public void test() throws FileNotFoundException {
        Document documentLoad = new ApacheTikaDocumentParser().parse(new FileInputStream("E:\\dataSource\\简历\\test.doc"));
//        TextSegment textSegment = document.toTextSegment();
//        Embedding content = embeddingModel.embed(document.toTextSegment()).content();
//        embeddingStore.add(content);

        EmbeddingStoreIngestor embeddingStoreIngestor = EmbeddingStoreIngestor.builder()
//                .documentTransformer(document -> {
//                    document.metadata().put("技能","java");
//                    return CleanDocumentComponent.cleanDocument(document);
//                })
                .documentSplitter(DocumentSplitters.recursive(500,50,new OpenAiTokenCountEstimator("gpt-4o-mini")))
                .textSegmentTransformer(textSegment -> {
                    System.out.println("切割后文档："+ textSegment.text());
                    return TextSegment.from(textSegment.text(),textSegment.metadata().put("name","赵参谋"));
                })
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        IngestionResult ingestionResult = embeddingStoreIngestor.ingest(documentLoad);
    }

    @GetMapping("/rag/test2")
    public String test2(){
        chatAssistant.chat("赵参谋拥有什么技能")
                .onRetrieved((List<Content> source)->{

                })
                .start();
        return null;
    }

}
