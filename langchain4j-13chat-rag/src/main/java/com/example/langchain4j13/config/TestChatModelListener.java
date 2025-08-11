package com.example.langchain4j13.config;

import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class TestChatModelListener implements ChatModelListener {

    @Override
    public void onRequest(ChatModelRequestContext requestContext) {
       String uuid = UUID.randomUUID().toString();
       requestContext.attributes().put("TraceID",uuid);
       log.info("请求参数requestContext:{}",requestContext+"\t"+uuid);
    }

    @Override
    public void onResponse(ChatModelResponseContext responseContext) {
       Object object = responseContext.attributes().get("TraceID");
       log.info("返回结果responseContext:{}",object);
    }

    @Override
    public void onError(ChatModelErrorContext errorContext) {
        log.info("请求异常ChatModelErrorContext:{}",errorContext);
    }
}
