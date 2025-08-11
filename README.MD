





# 				LangChain4j学习笔记

#### 官网地址

[简介 |LangChain4j](https://docs.langchain4j.dev/intro)

描述：

LangChain4j 的目标是简化将 LLM 集成到 Java 应用程序中的过程。

方法如下：

1. **统一 API：**LLM 提供程序（如 OpenAI 或 Google Vertex AI）和嵌入（向量）存储（如 Pinecone 或 Milvus） 使用专有 API。LangChain4j 提供了一个统一的 API，以避免为每个 API 学习和实现特定的 API。 要试验不同的 LLM 或嵌入存储，您可以轻松地在它们之间切换，而无需重写代码。 LangChain4j 目前支持 [15+ 个流行的 LLM 提供商](https://docs.langchain4j.dev/integrations/language-models/)和 [20+ 个嵌入商店](https://docs.langchain4j.dev/integrations/embedding-stores/)。
2. **综合工具箱：**自 2023 年初以来，社区一直在构建许多由 LLM 提供支持的应用程序。 识别常见的抽象、模式和技术。LangChain4j 已将这些改进为一个即用型包。 我们的工具箱包括从低级提示模板、聊天内存管理和函数调用等工具 到 Agent 和 RAG 等高级模式。 对于每个抽象，我们提供了一个接口以及基于通用技术的多个即用型实现。 无论您是构建聊天机器人还是开发具有从数据摄取到检索的完整管道的 RAG， LangChain4j 提供了多种选择。
3. **许多例子：**这些[示例](https://github.com/langchain4j/langchain4j-examples)展示了如何开始创建各种 LLM 驱动的应用程序。 提供灵感并使您能够快速开始构建。

LangChain4j 于 2023 年初在 ChatGPT 的炒作中开始开发。 我们注意到，许多 Python 和 JavaScript LLM 库和框架缺乏 Java 对应物。 我们必须解决这个问题！ 虽然我们的名字里有“LangChain”，但该项目融合了来自 LangChain、Haystack、 LlamaIndex 和更广泛的社区，都加入了我们自己的创新。\

为了更轻松地集成，LangChain4j 还包括与 [Quarkus](https://docs.langchain4j.dev/tutorials/quarkus-integration)、[Spring Boot](https://docs.langchain4j.dev/tutorials/spring-boot-integration)、[Helidon](https://docs.langchain4j.dev/tutorials/helidon-integration) 和 [Micronaut](https://docs.langchain4j.dev/tutorials/micronaut-integration) 的集成

#### 系统集成

##### 原生集成

```
<-- mavenbom -->
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>dev.langchain4j</groupId>
            <artifactId>langchain4j-bom</artifactId>
            <version>1.1.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<-- 基础依赖包 -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai</artifactId>
    <version>1.1.0</version>
</dependency>
<-- 高阶依赖包 -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId>
    <version>1.1.0</version>
</dependency>

```

##### SpringBoot集成

```
<-- 基础依赖包 -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-spring-boot-starter</artifactId>
    <version>1.1.0-beta7</version>
</dependency>
<-- 高阶依赖包 -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-spring-boot-starter</artifactId>
    <version>1.1.0-beta7</version>
</dependency>

```

##### 配置LLM

```java
@Configuration
public class LLMConfig {

    @Bean(name = "qwen")
    public ChatModel chatModelQwen(){
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("QWEN-API-KEY"))
                .modelName("qwen-plus")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();
    }
    
    @Bean(name = "deepseek")
    public ChatModel chatModelDeepSeek(){
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("DEEPSEEK-API-KEY"))
                .baseUrl("https://api.deepseek.com/v1")
                .modelName("deepseek-chat")
                .build();
    }
}
```

##### 启动测试

```java
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
```

#### LLM配置参数

| **参数名称**       | **数据类型**   | **取值范围**                                   | **默认值**                  | **功能描述**                                                 |
| ------------------ | -------------- | ---------------------------------------------- | --------------------------- | ------------------------------------------------------------ |
| `modelName`        | `String`       | 如 "gpt-3.5-turbo"、"gpt-4"、"gpt-4o" 等       | "gpt-3.5-turbo"             | 指定调用的 OpenAI 模型名称。不同模型在能力（推理精度、多模态支持）、上下文长度和成本上有差异，例如： - 轻量化任务选 `gpt-3.5-turbo`（速度快、成本低）； - 复杂任务选 `gpt-4` 或 `gpt-4o`（推理强、支持长文本）。 |
| `apiKey`           | `String`       | 有效的 OpenAI API 密钥                         | 无（必填参数）              | OpenAI 接口的访问凭证，用于身份验证。需从 [OpenAI 平台](https://platform.openai.com/) 获取，若为空则无法调用接口。 |
| `temperature`      | `Double`       | 0.0 ~ 2.0                                      | 0.7                         | 控制输出的随机性： - 值越高（如 1.5）：输出越发散、创意性越强，适合诗歌、头脑风暴等场景； - 值越低（如 0.1）：输出越确定、聚焦，适合精准问答、代码生成等要求严谨的场景。 |
| `maxTokens`        | `Integer`      | 1 ~ 模型最大上下文限制（如 GPT-4 支持 128000） | 无（默认由模型自动分配）    | 限制生成内容的最大令牌数（1 令牌≈1 个英文单词或 4 个中文汉字）。需注意：输入内容的令牌数 + 输出的 `maxTokens` 不能超过模型的最大上下文限制（否则会报错）。用于控制响应长度，避免冗余。 |
| `topP`             | `Double`       | 0.0 ~ 1.0                                      | 1.0                         | 核采样阈值：模型仅从累积概率超过该值的令牌中超过该值的令牌中选择。例如，`topP=0.9` 表示只考虑概率分布中前 90% 的可能词汇。与 `temperature` 配合使用（通常二选一），低 `temperature` + 高 `topP` 可平衡确定性和多样性。 |
| `frequencyPenalty` | `Double`       | -2.0 ~ 2.0                                     | 0.0                         | 频率惩罚：正值会降低重复出现过的令牌的生成概率，减少内容冗余。例如，`frequencyPenalty=0.5` 可避免模型反复重复同一句话，适合生成长文本（如文章、故事）。 |
| `presencePenalty`  | `Double`       | -2.0 ~ 2.0                                     | 0.0                         | 存在惩罚：正值会惩罚已出现过的主题 / 概念，鼓励模型引入新内容。例如，`presencePenalty=0.8` 适合头脑风暴场景，促进生成更多新颖视角。 |
| `stop`             | `List<String>` | 任意字符串列表（如 ["END", "###"]）            | 空列表                      | 停止序列：当模型生成的内容中出现列表中的字符串时，立即停止输出。用于精准控制输出截止点，避免无关内容（例如设置 `stop=["\n\n"]` 可限制输出为一段文本）。 |
| `timeout`          | `Duration`     | 正数（如 30 秒、60 秒）                        | 30 秒                       | API 请求的超时时间。若模型在规定时间内未返回响应，则触发超时错误。需根据网络状况和任务复杂度调整（复杂任务可设更长时间）。 |
| `maxRetries`       | `Integer`      | 0 ~ 自定义值（如 3、5）                        | 3                           | API 调用失败时的自动重试次数（如网络波动、服务临时不可用）。合理设置可提高系统鲁棒性，避免因临时故障导致调用失败。 |
| `baseUrl`          | `String`       | 合法 URL（如代理地址、私有部署地址）           | "https://api.openai.com/v1" | 自定义 API 端点。适用于使用代理服务器或企业私有部署的 OpenAI 兼容接口时，需修改为对应地址（例如国内代理地址、Azure OpenAI 接口地址）。 |
| `organizationId`   | `String`       | OpenAI 组织 ID                                 | 无                          | 可选参数，指定调用所属的 OpenAI 组织。用于团队账号的用量统计、权限管理（需在 OpenAI 平台提前配置组织）。 |
| `responseFormat`   | `String`       | "json_object"、"text"                          | "text"                      | 输出格式： - "json_object"：强制模型生成 JSON 结构内容（需提示模型输出 JSON），适合提取结构化数据； - "text"：默认文本格式，适合自由文本生成。 |

#### ChatStream：流式输出

```java
@Configuration
public class LLMConfig {

    @Bean(name = "qwen")
    public ChatModel chatModelQwen(){
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("QWEN-API-KEY"))
                .modelName("qwen-plus")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();
    }

    @Bean
    public StreamingChatModel streamingChatModel(){
        return OpenAiStreamingChatModel.builder()
                .apiKey(System.getenv("QWEN-API-KEY"))
                .modelName("qwen-plus")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();
    }

    @Bean
    public ChatAssistant chatAssistant(){
        return AiServices.create(ChatAssistant.class,streamingChatModel());
    }


}
```



```java
public interface ChatAssistant {

    String chat(String prompt);

    Flux<String> chatFlux(String prompt);
}
```

```java
/**
* 流式返回
*/
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
ss
/**
* 流式返回
*/
@GetMapping("/chatstream/chat3")
    public Flux<String> chat3(@RequestParam("prompt") String prompt){
        return chatAssistant.chatFlux(prompt);
}

```









#### ChatMemory：聊天缓存

| 类型               | 名称                      | 核心机制                                                     | 主要特点                                                     | 适用场景                                                     | 优势                                                         | 局限性                                                       |
| ------------------ | ------------------------- | ------------------------------------------------------------ | :----------------------------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 消息窗口聊天记忆   | `MessageWindowChatMemory` | 以**消息数量**为窗口限制，只保留最近的 N 条消息（包括用户、AI、函数调用等所有类型消息） | - 窗口大小通过`maxMessages`参数设置（如保留最近 10 条消息） - 直接基于消息条数截断历史，不考虑消息长度 | - 消息长度较均匀的场景（如短对话） - 对消息数量有明确控制需求的场景 | - 实现简单，易于理解和调试 - 内存占用可通过消息数量大致预估  | - 无法处理长消息（如大段文本、文档），可能因单条消息过长导致总上下文超限 - 同等消息数量下，总 Token 数可能波动较大 |
| Token 窗口聊天记忆 | `TokenWindowChatMemory`   | 以**Token 数量**为窗口限制，保留最近的、总 Token 数不超过阈值的消息（会自动截断超出 Token 限制的历史消息） | - 窗口大小通过`maxTokens`参数设置（如保留最近 2000 个 Token） - 需结合 Token 计数器（如`TokenCounter`）计算每条消息的 Token 数 - 优先保留最新消息，当总 Token 数超限时，从最早的消息开始删除 | - 消息长度差异大的场景（如混合短问答和长文档） - 需严格控制上下文 Token 总量的场景（如避免超出 LLM 的上下文窗口限制） | - 精准控制上下文总 Token 数，避免触发模型的 Token 限制 - 自适应不同长度的消息，确保有效信息不被截断 | - 实现较复杂，需依赖 Token 计数器（不同模型的 Token 计算规则可能不同） - 可能因频繁计算 Token 数带来轻微性能开销 |

- MessageWindowChatMemory

```java
@Bean(name = "chatMessageWindowChatMemory")
public ChatMemoryAssistant chatMemoryWindowChatMemory(ChatModel chatModel){
    return AiServices
            .builder(ChatMemoryAssistant.class)
            .chatModel(chatModel)
            .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(100))
            .build();
}
```

- TokenWindowChatMemory

```java
@Bean(name = "chatTokenChatMemory")
public ChatMemoryAssistant chatMemoryTokenChatMemory(ChatModel chatModel){
    TokenCountEstimator tokenCountEstimator = new OpenAiTokenCountEstimator("gpt-4");

    return AiServices
            .builder(ChatMemoryAssistant.class)
            .chatModel(chatModel)
            .chatMemoryProvider(memoryId -> TokenWindowChatMemory.withMaxTokens(1000,tokenCountEstimator))
            .build();
}
```

- 使用

```java
public interface ChatMemoryAssistant {

    String chatWithMemory(@MemoryId Long userId, @UserMessage String prompt);
}
```



#### ChatMemoryPersistence: 聊天记录持久化

配置Bean

```java
@Bean
public ChatPersistenceAssistant chatPersistenceAssistant(ChatModel chatModel){

    ChatMemoryProvider chatMemoryProvider = memoryId -> MessageWindowChatMemory.builder()
            .id(memoryId)
            .maxMessages(1000)
            .chatMemoryStore(redisChatMemoryStore)
            .build();

    return AiServices.builder(ChatPersistenceAssistant.class)
            .chatModel(chatModel)
            .chatMemoryProvider(chatMemoryProvider)
            .build();
}
```



配置持久化实现类

```java
@Component
public class RedisChatMemoryStore implements ChatMemoryStore {

    public static final String CHAT_MEMORY_PREFIX = "CHAT_MEMORY:";

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String chatValue = redisTemplate.opsForValue().get(CHAT_MEMORY_PREFIX+memoryId);
        return ChatMessageDeserializer.messagesFromJson(chatValue);
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        redisTemplate.opsForValue().set(CHAT_MEMORY_PREFIX+memoryId, ChatMessageSerializer.messagesToJson(messages));
    }

    @Override
    public void deleteMessages(Object memoryId) {
        redisTemplate.delete(CHAT_MEMORY_PREFIX+memoryId);
    }
}
```







#### Message：消息

###### type：类型

| 消息类型                  | 说明                   | 作用                               | 使用场景                                      |
| ------------------------- | ---------------------- | ---------------------------------- | --------------------------------------------- |
| **SystemMessage**         | 系统级指令消息         | 设置 LLM 的全局行为和角色          | 对话开始前配置模型（如 "你是一个专业的医生"） |
| **UserMessage**           | 用户输入的消息         | 传递用户的问题或指令               | 用户提问："感冒应该吃什么药？"                |
| **AIMessage**             | 模型生成的回复消息     | 包含 LLM 生成的文本或函数调用请求  | 模型回答："建议服用布洛芬..."                 |
| **FunctionCallMessage**   | 模型发起的函数调用消息 | 封装函数名和参数，触发外部工具调用 | 模型请求："调用 getWeather 函数查询北京天气"  |
| **FunctionResultMessage** | 函数调用结果消息       | 携带外部函数执行的结果数据         | 函数返回："北京当前温度 25°C，晴"             |

###### Prompt：提示词

- @V

```java
@SystemMessage("你是一位专业的中国法律顾问,只回答中国法律相关的问题。输出限制：对于其他领域的问题禁止回答，直接返回'抱歉，我只能回答中国法律相关的问题'")
@UserMessage("请回答以下法律问题：{{question}},字数控制在{{length}}以内")
String chat(@V("question")String question,@V("length")int length);
```

- @StructuredPrompt

```java
@Data
@StructuredPrompt("根据中国{{legal}}法律，解答以下问题：{{question}}")
public class LawPrompt {

    private String legal;

    private String question;
}
```

- PromptTemplate

```java
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
```





#### FunctionCalling(Tools)：模型回调



There is a concept known as "tools," or "function calling". It allows the LLM to call, when necessary, one or more available tools, usually defined by the developer. A tool can be anything: a web search, a call to an external API, or the execution of a specific piece of code, etc. LLMs cannot actually call the tool themselves; instead, they express the intent to call a specific tool in their response (instead of responding in plain text). We, as developers, should then execute this tool with the provided arguments and report back the results of the tool execution.

它允许 LLM 在必要时调用一个或多个可用工具，这些工具通常由开发人员定义。 工具可以是任何东西：Web 搜索、对外部 API 的调用或特定代码的执行等。 LLM 实际上不能自己调用该工具;相反，他们表达了意图 在其响应中调用特定工具（而不是以纯文本形式响应）。 然后，作为开发人员，我们应该使用提供的参数执行此工具并报告 工具执行的结果。



```java
@Bean
public FunctionAssistant functionAssistant(ChatModel chatModel){
    return AiServices.builder(FunctionAssistant.class)
            .chatModel(chatModel)
            .tools(new InvoiceHandler())
            .build();
}
```



```java
@Slf4j
public class InvoiceHandler {

    @Tool("根据用户提交的开票信息进行开票")
    public String handle(@P("公司名称")String companyName,@P("税号")String dutyNumber,@P("开票金额") String amount) throws Exception {
        log.info("companyName=={} dutyNumber=={} amount=={}",companyName,dutyNumber,amount);
        System.out.println(new WeatherService().getWeatherV2("101010100"));
        return "开票成功";
    }
}
```



#### Embedding(Vector) 向量存储

Embedding（向量嵌入）是机器学习和人工智能领域中一种核心技术，其本质是将**高维、离散或抽象的对象**（如文本、图像、音频、用户 ID 等）映射到一个**低维、连续的向量空间**，得到的这个低维向量就称为 “Embedding 向量”（或简称 “向量嵌入”）。



```java

@Bean
public QdrantClient qdrantClient(){
    QdrantGrpcClient.Builder grpcClientBuilder = QdrantGrpcClient.newBuilder("127.0.0.1",6334,false);
    return new QdrantClient(grpcClientBuilder.build());
}


@Bean
public EmbeddingStore<TextSegment> embeddingStore(){
    return QdrantEmbeddingStore.builder()
            .host("127.0.0.1")
            .port(6334)
            .collectionName("test-qdrant")
            .build();
}
```





```java
/**
* 插入数据
*/
@GetMapping("/embedding/add")
public String add(){
    String prompt = """
            咏鸡
            你好呀
            """;
    TextSegment segment1 = TextSegment.from(prompt);
    segment1.metadata().put("author","zzyy");
    Embedding embedding1=embeddingModel.embed(segment1).content();
    String result = embeddingStore.add(embedding1,segment1);
    return  result;
}

/**
* 查询数据
*/
@GetMapping("/embedding/query")
public void query1(){
    Embedding queryEmbedding = embeddingModel.embed("咏鸡说的是什么").content();
    EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
            .queryEmbedding(queryEmbedding)
            .maxResults(1)
            .build();
    EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(embeddingSearchRequest);
    System.out.println(searchResult.matches().get(0).embedded().text());
}

/**
* 查询数据
*/
@GetMapping("/embedding/query2")
public void query2(){
    Embedding queryEmbedding = embeddingModel.embed("咏鸡").content();
    EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
            .queryEmbedding(queryEmbedding)
            .filter(metadataKey("author").isEqualTo("zzyy"))
            .maxResults(1)
            .build();

    EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(embeddingSearchRequest);

    System.out.println(searchResult.matches().get(0).embedded().text());
}
```





#### RAG

RAG（Retrieval-Augmented Generation，检索增强生成）是一种结合**信息检索**与**大语言模型（LLM）生成能力**的技术，核心目标是让 AI 在生成回答时，能基于**外部知识库中的准确、最新信息**，而非仅依赖模型训练时学到的（可能过时或不完整的）内置知识。



##### Core API 核心API

负责文件转换存储



###### Document 文档

一个`Document`类代表整个文档，例如单个PDF文件或网页。目前，`Document`只能表示文本信息，但未来的更新将使其也能支持图像和表格。



###### MetaData 元数据

个`Document`都包含`Metadata`。它存储有关`Document`的元信息，例如名称、来源、上次更新日期、所有者或任何其他相关详细信息。



###### DocumentLoader 文档加载器

你可以从一个`String`创建一个`Document`，但更简单的方法是使用我们库中包含的文档加载器之一



| FileSystemDocumentLoader         | 来自`langchain4j`模块的`FileSystemDocumentLoader`            |
| -------------------------------- | ------------------------------------------------------------ |
| ClassPathDocumentLoader          | 来自`langchain4j`模块的`ClassPathDocumentLoader`             |
| UrlDocumentLoader                | 来自 `langchain4j` 模块的 `UrlDocumentLoader`                |
| AmazonS3DocumentLoader           | 来自`langchain4j-document-loader-amazon-s3`模块的`AmazonS3DocumentLoader` |
| AzureBlobStorageDocumentLoader   | 来自`langchain4j-document-loader-azure-storage-blob`模块的`AzureBlobStorageDocumentLoader` |
| GitHubDocumentLoader             | 来自`langchain4j-document-loader-github`模块的`GitHubDocumentLoader` |
| GoogleCloudStorageDocumentLoader | 来自 `langchain4j-document-loader-google-cloud-storage` 模块的 `GoogleCloudStorageDocumentLoader` |
| SeleniumDocumentLoader           | 来自`langchain4j-document-loader-selenium`模块的`SeleniumDocumentLoader` |
| PlaywrightDocumentLoader         | 来自`langchain4j-document-loader-playwright`模块的`PlaywrightDocumentLoader` |
| TencentCosDocumentLoader         | 来自 `langchain4j-document-loader-tencent-cos` 模块的 `TencentCosDocumentLoader` |



###### Document Parser 文档解析器

文档可以表示各种格式的文件，如PDF、DOC、TXT等



| TextDocumentParser         | 来自`langchain4j`模块的`TextDocumentParser`，它可以解析纯文本格式的文件（例如TXT、HTML、MD等） |
| -------------------------- | ------------------------------------------------------------ |
| ApachePdfBoxDocumentParser | 来自 `langchain4j-document-parser-apache-pdfbox` 模块的 `ApachePdfBoxDocumentParser`，它可以解析PDF文件 |
| ApachePoiDocumentParser    | 来自`langchain4j-document-parser-apache-poi`模块的`ApachePoiDocumentParser`，它可以解析微软办公软件文件格式（如DOC、DOCX、PPT、PPTX、XLS、XLSX等）。 |
| ApacheTikaDocumentParser   | 来自`langchain4j-document-parser-apache-tika`模块的`ApacheTikaDocumentParser`，它可以自动检测和解析几乎所有现有的文件格式。 |



###### Document Transformer 文档转换器

实现可执行多种文档转换：清洗，过滤，丰富内容，总结



###### Text Segment 文本片段

**TextSegment（文本片段）** 指的是将原始文档（如 PDF、Word、TXT 等）分割后得到的**较小、独立的文本单元**。它是连接原始文档与向量数据库、大模型之间的核心中间产物，直接影响检索精度和最终回答质量。



###### Document Splitter 文档拆分器



| DocumentByParagraphSplitter | 按段落分割器:按段落分隔符（默认`\n\n`，可自定义）分割，一个段落为一个片段 |
| --------------------------- | ------------------------------------------------------------ |
| DocumentByLineSplitter      | 按行分割器: 按换行符（`\n`/`\r\n`）分割，一行一个片段        |
| DocumentBySentenceSplitter  | 按句子分割器:按句子终结标点（中文`。？！`、英文`.?!`）分割，一个句子为一个片 |
| DocumentByWordSplitter      | 按单词分割器:其边界通常由**空格、标点符号**或**特定语言的分词规则**界定 |
| DocumentByCharacterSplitter | 按字符分割器:以 “字符” 为最小单位，按预设的`chunkSize`（单片段字符数）将文本均匀分割 |
| DocumentByRegexSplitter     | 按正则表达式分割器：是一种基于**自定义正则表达式（Regular Expression）** 的文档分割工具，它通过匹配文本中符合特定模式的内容作为分隔符 |
| DocumentSplitters.recursive | 递归字符分割器：从语义完整性高到低）递归分割文本，直到所有片段长度符合 `chunkSize`（目标片段长度）要求 |



angChain4j 的 `recursive()` 分割器针对不同语言预设了分隔符列表，以中文和英文为例：

| 优先级    | 中文分隔符（示例）         | 英文分隔符（示例）       | 语义单元    |
| --------- | -------------------------- | ------------------------ | ----------- |
| 1（最高） | `\n\n`（空行，段落分隔）   | `\n\n`（空行，段落分隔） | 段落        |
| 2         | `\n`（换行，行分隔）       | `\n`（换行，行分隔）     | 行          |
| 3         | `。` `？` `！`（句子终结） | `. ``? ``! `（句子终结） | 句子        |
| 4         | `，` `；`（句内停顿）      | `, ``; `（句内停顿）     | 子句        |
| 5（最低） | 空（按字符分割）           | （空格，单词分隔）       | 单词 / 字符 |



1. **适配模型窗口**：`chunkSize` 建议设置为模型最大 token 数的 70%-80%（如模型支持 1000 token，可设 500-600 字符，预留生成空间）。
2. **合理设置重叠**：`chunkOverlap` 建议为 `chunkSize` 的 10%-20%（如 500 字符片段重叠 50-100 字符），确保跨片段的语义连贯性（如长句被分割后仍能通过重叠部分关联）。
3. **自定义分隔符**：对特殊文档（如含自定义标记 `### 章节 ###`），可通过 `separators` 参数插入高优先级分隔符，例如：



###### Text Segment Transformer 文本片段转换器

在每个`TextSegment`中加入`Document`标题或简短摘要



###### Embedding Model  嵌入模型

定义向量模型化



###### Embedding Store 嵌入存储

定义向量存储器



###### Embedding Store Ingestor 嵌入存储摄取器



###### Example 示例

```java
@GetMapping("/rag/test")
    public void test() throws FileNotFoundException {
        Document documentLoad = new ApacheTikaDocumentParser().parse(new FileInputStream("E:\\dataSource\\简历\\test.doc"));
//        TextSegment textSegment = document.toTextSegment();
//        Embedding content = embeddingModel.embed(document.toTextSegment()).content();
//        embeddingStore.add(content);

        EmbeddingStoreIngestor embeddingStoreIngestor = EmbeddingStoreIngestor.builder()
//                .documentTransformer(document -> {
//                    document.metadata().put("技能","java");
//                    return CleanDocumentComponent.cleanDocument(document);
//                })
                .documentSplitter(DocumentSplitters.recursive(500,50,new OpenAiTokenCountEstimator("gpt-4o-mini")))
                .textSegmentTransformer(textSegment -> {
                    System.out.println("切割后文档："+ textSegment.text());
                    return TextSegment.from(textSegment.text(),textSegment.metadata().put("name","测试"));
                })
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        IngestionResult ingestionResult = embeddingStoreIngestor.ingest(documentLoad);
    }
```







##### Retrieval Augmentor  检索增强器

etrieval Augmentor 是RAG管道的入口点。它负责使用从各种来源检索到的相关`Content`来增强`ChatMessage`

###### Query  查询器

表示 RAG 管道中的用户查询。它包含查询文本和查询元数据



Query Metadata  查询元数据

| 名称                    | 描述                                    |
| ----------------------- | --------------------------------------- |
| Metadata.userMessage()  | 应扩充的原始 `UserMessage`              |
| Metadata.chatMemoryId() | 一个带有 `@MemoryId` 注解的方法参数的值 |
| Metadata.chatMemory()   | 所有之前的 `ChatMessage`                |



Query Transformer 查询变换器

将给定的 `Query` 转换为一个或多个 `Query`。其目的是通过修改或扩展原始 `Query` 来提高检索质量。

| 名称                             | 描述                 |
| -------------------------------- | -------------------- |
| Query compression                | 查询压缩             |
| Query expansion                  | 查询扩展             |
| Query re-writing                 | 查询重写             |
| Step-back prompting              | 回退提示             |
| Hypothetical document embeddings | 假设文档嵌入（HyDE） |



###### Content Retriever 内容检索器

ContentRetriever 使用给定的Query 从底层数据源检索content

| 名称                                  | 描述                 |
| ------------------------------------- | -------------------- |
| Embedding store                       | 嵌入存储             |
| Full-text search engine               | 全文搜索引擎         |
| Hybrid of vector and full-text search | 向量与全文搜索的混合 |
| Web Search Engine                     | 网页搜索引擎         |
| Knowledge graph                       | 知识图谱             |
| SQL database SQL                      | 数据库               |



###### Quert Router 查询路由器

QueryRouter负责Query路由到合适的ContentRetriever

| 名称                        | 描述                                                         |
| --------------------------- | ------------------------------------------------------------ |
| Default Query Router        | 默认查询路由器:它将每个 `Query` 路由到所有已配置的 ContentRetriever |
| Language Model Query Router | 语言模型查询路由器:使用大语言模型（LLM）来决定将给定的 `Query` 路由到何处 |



###### Content Aggregator 内容聚合

ContentAggregator 负责聚合来自以下的多个已排序的`Content`列表

| 名称                          | 描述                                                         |
| ----------------------------- | ------------------------------------------------------------ |
| Default Content Aggregator    | 默认内容聚合器：它执行两阶段的互反排序融合 (RRF)             |
| Re-Ranking Content Aggregator | 重排内容聚合器：使用`ScoringModel`（如Cohere）来执行重新排序 |



###### Content Injector 内容注入器

ContentInjector负责将content注入到UserMessage中

| 名称                     | 描述                                                         |
| ------------------------ | ------------------------------------------------------------ |
| Default Content Injector | 默认内容注入器：它只是将 `Content` 以 `Answer using the following information:` 为前缀追加到 `UserMessage` 的末尾 |



###### Parallelization  并行化

当只有一个 `Query` 和一个 `ContentRetriever` 时，`DefaultRetrievalAugmentor` 会在同一线程中执行查询路由和内容检索。否则，会使用一个 `Executor` 来并行处理。默认情况下，会使用一个经过修改的（`keepAliveTime` 为1秒而非60秒）`Executors.newCachedThreadPool()`，但你可以在创建 `DefaultRetrievalAugmentor` 时提供一个自定义的 `Executor` 实例



###### Accessing Sources 访问来源

在流式传输时，可以使用 `onRetrieved()` 方法指定一个 `Consumer<List<Content>>；



###### Example 示例

```java
@Configuration
public class RetrieverConfig {

    @Bean
    public EmbeddingStoreContentRetriever embeddingRetriever(EmbeddingModel embeddingModel, EmbeddingStore embeddingStore){
        return EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .maxResults(3) //返回条数
//                .dynamicMaxResults(query -> 3)
//                .minScore(0.75) //相似度阈值0-1
//                .dynamicMinScore(query -> 0.75)
//                .filter(metadataKey("name").isEqualTo("赵参谋")) //过滤器
//                .dynamicFilter(query -> {
//                    String userId = getUserId(query.metadata().chatMemoryId());
//                    return metadataKey("userId").isEqualTo(userId);
//                })
                .build();
    }

    @Value("${spring.datasource.url:jdbc:mysql://localhost:3306/your_database}")
    private String databaseUrl;

    @Value("${spring.datasource.username:root}")
    private String databaseUsername;

    @Value("${spring.datasource.password:password}")
    private String databasePassword;

    @Bean
    public SqlDatabaseContentRetriever sqlRetriever(ChatModel chatModel){
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(databaseUrl);
        dataSource.setUser(databaseUsername);
        dataSource.setPassword(databasePassword);

        return SqlDatabaseContentRetriever.builder()
                .dataSource(dataSource)
                .chatModel(chatModel)
                .build();
    }
}
```





```java
 @Bean
    public ChatAssistant assistant( ChatModel chatModel,
                                    StreamingChatModel streamingChatModel,
                                    EmbeddingStoreContentRetriever embeddingStoreContentRetriever,
                                    SqlDatabaseContentRetriever sqlDatabaseContentRetriever){
//        Map<ContentRetriever, String> retrieverToDescription = new HashMap<>();
//        retrieverToDescription.put(embeddingStoreContentRetriever, "Embedding Store");
//        retrieverToDescription.put(sqlDatabaseContentRetriever, "SQL Database");
//        QueryRouter queryRouter = new LanguageModelQueryRouter(chatModel,retrieverToDescription);
        QueryRouter queryRouter = new DefaultQueryRouter(embeddingStoreContentRetriever,sqlDatabaseContentRetriever);
        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder().queryRouter(queryRouter).build();

        return AiServices.builder(ChatAssistant.class)
                .chatModel(chatModel)
                .streamingChatModel(streamingChatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(50))
                .retrievalAugmentor(retrievalAugmentor)
//                .contentRetriever(embeddingStoreContentRetriever)
                .build();
    }
```
