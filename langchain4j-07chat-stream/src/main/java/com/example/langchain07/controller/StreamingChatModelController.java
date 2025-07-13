package com.example.langchain07.controller;

import com.example.langchain07.service.ChatAssistant;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
public class StreamingChatModelController {

    @Resource
    private StreamingChatModel streamingChatModel;

    @Resource
    private ChatAssistant chatAssistant;

    //http:://127.0.0.1:9007/chatstream/chat?prompt=
    @GetMapping("/chatstream/chat")
    public Flux<String> chat(@RequestParam("prompt") String prompt){
        return Flux.create(emitter ->{
            streamingChatModel.chat(prompt, new StreamingChatResponseHandler() {
                @Override
                public void onPartialResponse(String partialResponse) {
                    emitter.next(partialResponse);
                }

                @Override
                public void onCompleteResponse(ChatResponse completeResponse) {
                    emitter.complete();
                }

                @Override
                public void onError(Throwable error) {
                    emitter.error(error);
                }
            });
        });
    }

    //http:://127.0.0.1:9007/chatstream/chat2?prompt=
    @GetMapping("/chatstream/chat2")
    public void chat2(@RequestParam("prompt") String prompt){
            streamingChatModel.chat(prompt, new StreamingChatResponseHandler() {
                @Override
                public void onPartialResponse(String partialResponse) {
                    System.out.println(partialResponse);
                }

                @Override
                public void onCompleteResponse(ChatResponse completeResponse) {
                    System.out.println("---response over"+completeResponse);
                }

                @Override
                public void onError(Throwable error) {
                    error.printStackTrace();
                }
            });
    }

    //http:://127.0.0.1:9007/chatstream/chat?prompt=
    @GetMapping("/chatstream/chat3")
    public Flux<String> chat3(@RequestParam("prompt") String prompt){
        return chatAssistant.chatFlux(prompt);
    }
}
