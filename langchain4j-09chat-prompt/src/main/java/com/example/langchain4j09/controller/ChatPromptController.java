package com.example.langchain4j09.controller;

import com.example.langchain4j09.service.LawAssistant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ChatPromptController {
    @Resource
    private LawAssistant lawAssistant;

    @GetMapping("/chatprompt/test1")
    public String test1(){
        String chat = lawAssistant.chat("什么是知识产权",2000);
        System.out.println(chat);

        String chat2 = lawAssistant.chat("什么是java",2000);
        System.out.println(chat2);

        String chat3 = lawAssistant.chat("介绍一下西瓜和芒果",2000);
        System.out.println(chat3);

        String chat4 = lawAssistant.chat("飞机发动机原理",2000);
        System.out.println(chat4);

        return "success:" + "<br> \n\n chat: "+chat+"<br> \n\n chat2: "+chat2;
    }
}
