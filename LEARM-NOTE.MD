# LangChain4j学习笔记

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



