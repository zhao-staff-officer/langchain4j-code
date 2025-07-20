package com.example.langchain4j11.config;

import com.example.langchain4j11.service.FunctionAssistant;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class LLMConfig {

    @Bean
    public ChatModel chatModel(){
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("QWEN-API-KEY"))
                .modelName("qwen-plus")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();
    }

    @Bean
    public FunctionAssistant functionAssistant(ChatModel chatModel){
        ToolSpecification toolSpecification = ToolSpecification.builder()
                .name("开发票助手")
                .description("根据用户提交的开票信息，开局发票")
                .parameters(JsonObjectSchema.builder()
                        .addStringProperty("companyName","公司名称")
                        .addStringProperty("dutyNumber","税号序列")
                        .addStringProperty("amount","开票金额，保留两位有效数字")
                        .build())
                .build();
        ToolExecutor toolExecutor = (toolExecutionRequest, memoryId) -> {
            System.out.println(toolExecutionRequest.id());
            System.out.println(toolExecutionRequest.name());
            System.out.println(toolExecutionRequest.arguments());
            return "开局成功";
        };
        return AiServices.builder(FunctionAssistant.class)
                .chatModel(chatModel)
                .tools(Map.of(toolSpecification,toolExecutor))
                .build();
    }



}
