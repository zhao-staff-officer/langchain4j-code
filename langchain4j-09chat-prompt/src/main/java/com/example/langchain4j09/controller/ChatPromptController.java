package com.example.langchain4j09.controller;

import com.example.langchain4j09.entity.LawPrompt;
import com.example.langchain4j09.service.LawAssistant;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class ChatPromptController {
    @Resource
    private LawAssistant lawAssistant;

    @Resource
    private ChatModel chatModel;

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

    @GetMapping("/chatprompt/test2")
    public String test2(){
        LawPrompt prompt = new LawPrompt();
        prompt.setLegal("知识产权");
        prompt.setQuestion("什么是TRIPS协议？");

        String chat = lawAssistant.chat(prompt);
        System.out.println(chat);
        return "success:"+"<br> \n\n chat: "+chat;
    }

    @GetMapping("/chatprompt/test3")
    public String test3(){
        String role = "外科医生";
        String question = "牙疼";
        PromptTemplate template = PromptTemplate.from("你是一个{{it}}助手,{{question}}怎么办");
        Prompt prompt = template.apply(Map.of("it",role,"question",question));
        UserMessage userMessage = prompt.toUserMessage();
        ChatResponse chatResponse =  chatModel.chat(userMessage);
        System.out.println(chatResponse.aiMessage().text());
        return "success:"+"<br> \n\n chat: "+chatResponse.aiMessage().text();
    }
}
