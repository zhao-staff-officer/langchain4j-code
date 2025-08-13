package com.example.langchain4j.controller;

import com.example.langchain4j.service.McpService;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;


@RestController
public class McpController {

    @Autowired
    private StreamingChatModel streamingChatModel;

    @GetMapping("/mcp/chat")
    public Flux<String> chat(@RequestParam ("question") String question) {

        //构建McpTransport协议
        McpTransport transport = new StdioMcpTransport.Builder()
                .command(List.of("cmd","/c","npx","-y","@baidumap/mcp-server-baidu-map"))
                .environment(Map.of("BAIDU_MAP_API_KEY ",System.getenv("BAIDU-API-KEY")))
                .logEvents(true)
                .build();

        //创建McpClient
        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();

        //创建工具集和原生的FunctionCalling类似
        McpToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(mcpClient)
                .build();

        //通过AiServices创建服务
        McpService mcpService = AiServices.builder(McpService.class)
                .streamingChatModel(streamingChatModel)
                .toolProvider(toolProvider)
                .build();

        return mcpService.chat(question);
    }


}
