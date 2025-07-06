package com.example.langchain4j04.controller;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.output.TokenUsage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class lowApiController {

    @Resource(name = "qwen")
    private ChatModel chatModelQwen;

    @Resource(name = "deepseek")
    private ChatModel chatModelDeepSeek;

    //http://localhost:9004/lowapi/qwen
    @GetMapping("/lowapi/qwen")
    public String qwenCall(@RequestParam(value = "question",defaultValue = "你是谁") String question){
        String  result = chatModelQwen.chat(question);
        log.info("调用大模型回复："+result);
        return  result;
    }

    //http://localhost:9004/lowapi/deepseek
    @GetMapping("/lowapi/deepseek")
    public String deepSeekCall(@RequestParam(value = "question",defaultValue = "你是谁") String question){
        ChatResponse chatResponse =  chatModelDeepSeek.chat(UserMessage.from(question));
        String result = chatResponse.aiMessage().text();
        log.info("调用大模型回复："+result);
        TokenUsage tokenUsage = chatResponse.tokenUsage();
        log.info("调用大模型token："+tokenUsage);
        result = result + "\t\n" +tokenUsage;
        return  result;
    }
}
