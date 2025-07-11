package com.example.langchain4j06.controller;

import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.output.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Base64;

@Slf4j
@RestController
public class WanxImageModelController {

    @Autowired
    private WanxImageModel wanxImageModel;

    //http://localhost:9006/image/create2
    @GetMapping("/image/create2")
    public String qwenCall(@RequestParam(value = "question",defaultValue = "你是谁") String question) throws IOException {

        Response<Image>  imageResponse = wanxImageModel.generate("美女打桌球");

        return  imageResponse.content().url().toString();
    }
}
