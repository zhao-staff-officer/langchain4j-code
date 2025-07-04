package com.example.langchain4j01helloworld.controller;

import dev.langchain4j.model.chat.ChatModel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloLangChain4JController {

    @Resource
    private ChatModel chatModel;

    @GetMapping("/langchain/hello")
    public String hello(@RequestParam(value = "question",defaultValue = "你是谁") String question){
        String  result = chatModel.chat(question);
        log.info("调用大模型回复："+result);
        return  result;
    }
}
