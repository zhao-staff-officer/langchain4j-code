package com.example.langchain4j06.controller;

import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Base64;

@Slf4j
@RestController
public class ModelImageApiController {

    @Autowired
    private ChatModel chatModelQwen;

    @Value("classpath:static/image/test.png")
    private Resource resource;


    //http://localhost:9006/image/call
    @GetMapping("/image/call")
    public String qwenCall(@RequestParam(value = "question",defaultValue = "你是谁") String question) throws IOException {
        byte[]  byteArray = resource.getContentAsByteArray();
        String imageBase64Str = Base64.getEncoder().encodeToString(byteArray);
        UserMessage message =  UserMessage.from(
                TextContent.from("介绍图片信息"),
                ImageContent.from(imageBase64Str,"image/png"));
        ChatResponse response = chatModelQwen.chat(message);
        log.info("调用大模型回复："+response.aiMessage().text());
        return  response.aiMessage().text();
    }
}
