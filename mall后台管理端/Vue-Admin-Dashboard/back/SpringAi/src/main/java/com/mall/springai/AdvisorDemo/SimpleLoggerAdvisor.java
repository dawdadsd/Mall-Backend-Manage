package com.mall.springai.AdvisorDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.api.*;
import reactor.core.publisher.Flux;

public class SimpleLoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {
    private static final Logger logger = LoggerFactory.getLogger(SimpleLoggerAdvisor.class);

    @Override
    public String getName() {
        return "SimpleLoggerAdvisor"; // 助手名字
    }

    @Override
    public int getOrder() {
        return 0; // 执行顺序
    }

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        logger.debug("请求前: {}", advisedRequest); // 打印请求
        AdvisedResponse response = chain.nextAroundCall(advisedRequest); // 交给下一个
        logger.debug("请求后: {}", response); // 打印结果
        return response;
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        return null;
    }
}
