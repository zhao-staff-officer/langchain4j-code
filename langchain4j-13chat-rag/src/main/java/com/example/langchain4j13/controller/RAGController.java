package com.example.langchain4j13.controller;

import com.example.langchain4j13.service.ChatAssistant;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Slf4j
@RestController
public class RAGController {


    @Resource
    private EmbeddingStore<TextSegment> embeddingStore;

    @Resource
    private ChatAssistant chatAssistant;

    @GetMapping("/rag/test")
    public String test() throws FileNotFoundException {
        Document document = new ApacheTikaDocumentParser().parse(new FileInputStream("E:\\dataSource\\简历\\test.docx"));
        TextSegment textSegment = document.toTextSegment();
        EmbeddingStoreIngestor.ingest(document,embeddingStore);

        String result = chatAssistant.chat("拥有什么技能");
        log.info("输出请求答案：{}",result);
        return result;
    }

}
