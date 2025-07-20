package com.example.langchain4j11.controller;

import com.example.langchain4j11.service.FunctionAssistant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ChatFunctionCallingController {
    @Resource
    private FunctionAssistant functionAssistant;


    @GetMapping("/chatfunction/test1")
    public String test1(){
        String chat = functionAssistant.chat("开张发票,公司：公司1 税号：test1 金额6666");
        System.out.println(chat);
        return "success"+chat;
    }

}
