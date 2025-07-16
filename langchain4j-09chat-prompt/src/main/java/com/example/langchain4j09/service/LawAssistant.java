package com.example.langchain4j09.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface LawAssistant {

    @SystemMessage("你是一位专业的中国法律顾问,只回答中国法律相关的问题。输出限制：对于其他领域的问题禁止回答，直接返回'抱歉，我只能回答中国法律相关的问题'")
    @UserMessage("请回答以下法律问题：{{question}},字数控制在{{length}}以内")
    String chat(@V("question")String question,@V("length")int length);
}
