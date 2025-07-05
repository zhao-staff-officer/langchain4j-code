package com.example.langchain4j02.controller;

import dev.langchain4j.model.chat.ChatModel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloLangChain4JController {

    @Resource(name = "qwen")
    private ChatModel chatModelQwen;

    @Resource(name = "deepseek")
    private ChatModel chatModelDeepSeek;

    //http://localhost:9002/mulit/qwen
    @GetMapping("/mulit/qwen")
    public String qwenCall(@RequestParam(value = "question",defaultValue = "你是谁") String question){
        String  result = chatModelQwen.chat(question);
        log.info("调用大模型回复："+result);
        return  result;
    }

    //http://localhost:9002/mulit/deepseek
    @GetMapping("/mulit/deepseek")
    public String deepSeekCall(@RequestParam(value = "question",defaultValue = "你是谁") String question){
        String  result = chatModelDeepSeek.chat(question);
        log.info("调用大模型回复："+result);
        return  result;
    }
}
