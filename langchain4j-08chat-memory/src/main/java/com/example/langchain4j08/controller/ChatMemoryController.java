package com.example.langchain4j08.controller;

import com.example.langchain4j08.service.ChatAssistant;
import com.example.langchain4j08.service.ChatMemoryAssistant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
public class ChatMemoryController {

    @Resource(name = "chat")
    private ChatAssistant chatAssistant;

    @Resource(name = "chatMessageWindowChatMemory")
    private ChatMemoryAssistant chatMessageWindowAssistant;

    @Resource(name = "chatTokenChatMemory")
    private ChatMemoryAssistant chatTokenAssistant;


    @GetMapping("/chatmemory/test1")
    public String chat(){
        String as1 =chatAssistant.chat("你好，我叫张三");
        log.info("as1返回结果：{}",as1);

        String as2 =chatAssistant.chat("我的名字是什么");
        log.info("as2返回结果：{}",as2);
        return "success: as1:"+as1+" as2:"+as2;
    }

    @GetMapping("/chatmemory/test2")
    public String chat2(){
        String as1 =chatMessageWindowAssistant.chatWithMemory(1L,"你好，我叫张三");
        String as2 =chatMessageWindowAssistant.chatWithMemory(1L,"我的名字是什么");
        log.info("as2返回结果：{}",as1);
        return "success:  as2："+as2;
    }

    @GetMapping("/chatmemory/test3")
    public String chat3(){
        String as1 =chatTokenAssistant.chatWithMemory(1L,"你好，张三是个善良的人");
        String as2 =chatTokenAssistant.chatWithMemory(1L,"请问张三是善良的人吗");
        log.info("as2返回结果：{}",as1);
        return "success:  as2："+as2;
    }

}