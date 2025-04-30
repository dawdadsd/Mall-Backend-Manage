package com.mall.springai.Demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class AiController {
    private final ChatClient chatClient;
    public AiController(ChatClient.Builder chatClientBuilder)
    {
        this.chatClient = chatClientBuilder.build();
    }
    @GetMapping("/ai")
    Map<String,String> completion(@RequestParam String message , @RequestParam String tone) {
        return Map.of("completion",
                Objects.requireNonNull(this.chatClient
                        .prompt()
                        .system(sp -> sp.param("tone", tone))
                        .user(message)
                        .call()
                        .content()
                        ));
    }
    @GetMapping(value = "/ai/stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> streamCompletion(@RequestParam String message)
    {
        return this.chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }
    @GetMapping("/ai/full")
    public ChatResponse completionFullResponse(@RequestParam String message)
    {
        return this.chatClient
                .prompt()
                .user(message)
                .call()
                .chatResponse();
    }
    record ActorFilms(String actor, List<String> movies){}
    @GetMapping("/ai/actor-films")
    public ActorFilms getActorFilms(@RequestParam String actorName)
    {
        // 构建包含转义花括号的 Prompt 字符串
        String userPrompt = "生成一个关于中文的json格式 " + actorName +
                ". Respond in JSON format matching the ActorFilms structure: ";

        // 打印 Prompt 以便调试
        System.out.println("--- Sending ActorFilms Prompt: ---");
        System.out.println(userPrompt);
        System.out.println("----------------------------------");
        
        try {
            return chatClient.prompt()
                    .user(userPrompt) // 使用转义后的 Prompt
                    .call()
                    .entity(ActorFilms.class);
        } catch (Exception e) {
            // 添加错误处理
            System.err.println("Error parsing ActorFilms response: " + e.getMessage());
            e.printStackTrace();
            // 返回一个表示错误的 ActorFilms 对象或 null
            return new ActorFilms(actorName, List.of("Error retrieving movies: " + e.getMessage()));
        }
    }
}
