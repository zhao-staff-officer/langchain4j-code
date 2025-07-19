package com.example.langchain4j10.controller;

import com.example.langchain4j10.service.ChatPersistenceAssistant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ChatPersistenceController {

    @Resource
    private ChatPersistenceAssistant chatPersistenceAssistant;


    @GetMapping("/chatpersistence/redis")
    public String cha(){
        chatPersistenceAssistant.chat(1L,"你好！我的名字是redis");
        chatPersistenceAssistant.chat(2L,"你好！我的名字是nacos");
        String chat1 = chatPersistenceAssistant.chat(1L,"我的名字是什么");
        log.info(chat1);

        String chat2 = chatPersistenceAssistant.chat(2L,"我的名字是什么");
        log.info(chat2);

        return "test success";
    }
}
