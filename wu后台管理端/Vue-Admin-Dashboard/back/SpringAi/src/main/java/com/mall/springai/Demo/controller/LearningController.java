package com.mall.springai.Demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor; // 需要导入 Advisor 常量
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:5173") // 允许来自指定源的跨域请求
@RequestMapping("/learn")
public class LearningController {

    private final ChatClient learningAssistantClient;

    /**
     * 注入配置好的、带有学习助手角色的 ChatClient。
     * 使用 @Qualifier 指定 Bean 名称，以防存在多个 ChatClient Bean。
     */
    public LearningController(@Qualifier("learningAssistantClient") ChatClient learningAssistantClient) {
        this.learningAssistantClient = learningAssistantClient;
    }

    /**
     * 处理编程学习问题。
     * @param request 包含问题和可选会话ID的请求体。
     * @return AI 学习助手的回答。
     */
    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> askLearningQuestion(@RequestBody LearningRequest request) {
        String question = request.question();
        // 如果请求中没有提供 conversationId，则生成一个新的
        String conversationId = (request.conversationId() != null && !request.conversationId().isEmpty()) 
                                ? request.conversationId() 
                                : UUID.randomUUID().toString();
        try {
            // 调用 ChatClient，传入用户问题
            // 使用 .advisors() 在运行时设置参数，这里是为记忆功能设置 conversationId
            String responseContent = learningAssistantClient.prompt()
                    .user(question)
                    .advisors(advisor -> advisor
                            .param(MessageChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId))
                    .call()
                    .content();

            // 返回包含回答和会话ID的响应
            if(responseContent==null){
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(Map.of("answer", responseContent, "conversationId", conversationId));

        } catch (Exception e) {
            // 简单的错误处理
            e.printStackTrace(); // 打印错误堆栈
            return ResponseEntity.internalServerError().body(Map.of("error", "调用AI助手时出错: " + e.getMessage(), "conversationId", conversationId));
        }
    }

    /**
     * 用于接收请求的简单记录类。
     */
    public record LearningRequest(String question, String conversationId) {}

} 