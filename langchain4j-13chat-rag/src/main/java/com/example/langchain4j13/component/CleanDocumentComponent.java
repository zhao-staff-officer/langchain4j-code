package com.example.langchain4j13.component;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentTransformer;

import java.util.regex.Pattern;

public class CleanDocumentComponent implements DocumentTransformer {

    // 匹配所有空白字符（空格、换行、制表符等）的正则
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
    // 是否过滤清洗后内容为空的文档
    private final boolean filterEmpty;

    public CleanDocumentComponent() {
        this(true); // 默认过滤空文档
    }

    public CleanDocumentComponent(boolean filterEmpty) {
        this.filterEmpty = filterEmpty;
    }

    @Override
    public Document transform(Document document) {
        return cleanDocument(document);
    }


    /**
     * 清洗单个文档的内容
     */
    public static Document cleanDocument(Document document) {
        String originalContent = document.text();
        String cleanedContent = WHITESPACE_PATTERN.matcher(originalContent).replaceAll(" ").trim();
        return Document.from(cleanedContent, document.metadata());
    }



}
