package com.example.langchain4j04.controller;

import com.example.langchain4j04.service.ChatAssistant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HighApiController {
    @Resource
    private ChatAssistant chatAssistant;

    //http://localhost:9004/highapi/qwen
    @GetMapping("/highapi/qwen")
    public String qwenCall(@RequestParam(value = "question",defaultValue = "你是谁") String question){
        String  result = chatAssistant.chat(question);
        log.info("调用大模型回复："+result);
        return  result;
    }
}
