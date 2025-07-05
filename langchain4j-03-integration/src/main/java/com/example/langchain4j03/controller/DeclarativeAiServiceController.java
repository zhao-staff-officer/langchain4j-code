package com.example.langchain4j03.controller;

import com.example.langchain4j03.service.ChatAssistant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DeclarativeAiServiceController {
    @Resource
    private ChatAssistant chatAssistant;

    //http://localhost:9003/lc4j/boot/declarative
    @GetMapping("/lc4j/boot/declarative")
    public String qwenCall(@RequestParam(value = "question",defaultValue = "你是谁") String question){
        String  result = chatAssistant.chat(question);
        log.info("调用大模型回复："+result);
        return  result;
    }
}
