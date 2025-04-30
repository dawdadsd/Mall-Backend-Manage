package com.mall.springai.Demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    /**
     * 创建一个内存中的对话记忆存储 Bean。
     * 在生产环境中，你可能希望使用持久化的 ChatMemory 实现，例如 JdbcChatMemory 或 RedisChatMemory。
     */
    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    /**
     * 配置 ChatClient Bean。
     * - 设置默认的系统提示，定义 AI 助手的角色和行为。
     * - 添加 MessageChatMemoryAdvisor 以启用对话记忆。
     * @param builder 自动配置的 ChatClient Builder
     * @param chatMemory 对话记忆存储
     * @return 配置好的 ChatClient 实例
     */
    @Bean
    public ChatClient learningAssistantClient(ChatClient.Builder builder, ChatMemory chatMemory) {
        return builder
                // 设置默认系统提示，定义助手的角色和基本行为
                .defaultSystem("你是一位专业的编程学习助手。请耐心、清晰地解答用户的编程问题，解释代码逻辑，" +
                        "并提供相关的代码示例（如果适用),输出的文档要求是符合markdown的文本" +
                        "。请始终使用中文回答。")
                // 添加默认的 Advisor 来处理对话记忆
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
                .build();
    }
}
