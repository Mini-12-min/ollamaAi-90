项目简介
本项目旨在构建一个完全离线的智能问答系统，通过整合 DeepSeek 推理模型、Spring Boot 后端框架以及多种可视化 UI 工具，实现本地部署、高效推理和便捷交互。系统支持实时对话、历史记录持久化，并提供多样化的用户界面选择。
核心功能
离线部署：基于 Ollama 部署 DeepSeek-R1 模型，无需联网即可完成推理。
多端交互：支持 PageAssistn 浏览器插件、Chatbox 桌面端和 Cherry Studio API 工具。
实时响应：通过 SSE（Server-Sent Events）实现服务器推送，确保对话流畅。
数据持久化：使用 MySQL 存储用户会话记录，支持历史消息回溯。
易于扩展：模块化架构设计，方便后续功能迭代与模型替换。

快速开始
1. 环境准备
1.1 安装依赖工具
Ollama：用于部署 DeepSeek 模型，下载地址：Ollama 官网
JDK 17：确保环境变量 JAVA_HOME 配置正确
Maven 3.6+：项目构建工具
MySQL 5.7：数据库服务（本地或远程均可）
Navicat/DBeaver：数据库管理工具
1.2 部署 DeepSeek 模型
启动 Ollama 服务（默认端口：11434）。
拉取并部署 DeepSeek-R1 模型：
bash
运行
ollama pull deepseek-r1
验证模型部署是否成功：
bash
运行
ollama run deepseek-r1
输入问题后若能正常响应，说明模型部署完成。
2. 项目构建与配置
2.1 克隆项目
bash
运行
git clone <项目仓库地址>
cd DeepSeek-SpringAI-Offline-System
2.2 配置数据库
创建 MySQL 数据库：
sql
CREATE DATABASE deepseek_chat DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
修改 application.yml 中的数据库配置：
yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/deepseek_chat?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
2.3 构建项目
bash
运行
mvn clean package -Dmaven.test.skip=true
2.4 启动后端服务
bash
运行
java -jar target/deepseek-springai-offline-system-0.0.1-SNAPSHOT.jar
默认端口：8080，可通过 server.port 配置修改。
3. 前端交互
3.1 使用 Chatbox 桌面端
下载并安装 Chatbox：Chatbox 官网
配置 API 地址：http://localhost:8080/api/chat/stream
选择请求方式为 POST，发送 JSON 格式数据：
json
{
  "prompt": "你的问题",
  "userId": "user123"
}
3.2 使用 PageAssistn 浏览器插件
安装插件后，在配置页填写后端服务地址：http://localhost:8080
在浏览器中打开插件，输入问题即可实时对话。
3.3 使用 Cherry Studio 调试 API
创建新请求，选择 POST 方法，输入 URL：http://localhost:8080/api/chat/stream
设置请求头：Content-Type: application/json
发送请求体：
json
{
  "prompt": "介绍一下 Spring AI",
  "userId": "debugUser"
}
核心模块说明
1. 模型调用模块（Spring AI 集成）
通过 Spring AI 封装 Ollama 模型调用，核心代码：
java
运行
@Service
public class ChatService {
    private final OllamaChatClient chatClient;

    @Autowired
    public ChatService(OllamaChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public Flux<String> streamChat(String prompt, String userId) {
        // 构建对话历史
        List<ChatMessage> messages = buildChatHistory(userId);
        messages.add(new UserMessage(prompt));
        
        // 流式调用模型
        return chatClient.stream(ChatPrompt.from(messages))
                .map(ChatResponse::getResult)
                .map(Generation::getText);
    }
}
2. SSE 实时推送模块
基于 Spring WebFlux 实现 SSE 服务，核心代码：
java
运行
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @PostMapping("/stream")
    public SseEmitter streamChat(@RequestBody ChatRequest request) {
        SseEmitter emitter = new SseEmitter(-1L); // 无超时时间
        Flux<String> messageFlux = chatService.streamChat(request.getPrompt(), request.getUserId());
        
        messageFlux.subscribe(
                message -> {
                    try {
                        emitter.send(SseEmitter.event().data(message));
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }
                },
                error -> emitter.completeWithError(error),
                emitter::complete
        );
        
        return emitter;
    }
}
3. 数据持久化模块
使用 MyBatis-Plus 实现会话记录存储，核心代码：
java
运行
@Service
public class ChatRecordService {
    @Autowired
    private ChatRecordMapper chatRecordMapper;

    public void saveRecord(ChatRecord record) {
        chatRecordMapper.insert(record);
    }

    public List<ChatRecord> getHistoryByUserId(String userId) {
        return chatRecordMapper.selectList(
                new QueryWrapper<ChatRecord>().eq("user_id", userId).orderByDesc("create_time")
        );
    }
}
目录结构
plaintext
DeepSeek-SpringAI-Offline-System/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           ├── controller/  // 控制器层
│   │   │           ├── service/     // 服务层
│   │   │           ├── mapper/      // Mapper层
│   │   │           ├── model/       // 数据模型
│   │   │           └── config/      // 配置类
│   │   └── resources/
│   │       ├── application.yml      // 配置文件
│   │       └── mapper/              // MyBatis映射文件
│   └── test/                        // 测试代码
├── pom.xml                          // Maven依赖
└── README.md                        // 项目文档
常见问题
Ollama 模型调用失败：
检查 Ollama 服务是否启动，端口是否为 11434。
验证模型名称是否正确（deepseek-r1）。
SSE 连接断开：
检查前端请求是否正确设置 Accept: text/event-stream。
调整 SSE 超时时间（默认无超时）。
数据库连接失败：
确认 MySQL 服务是否启动，用户名 / 密码是否正确。
检查数据库 URL 中的端口和数据库名是否匹配。
