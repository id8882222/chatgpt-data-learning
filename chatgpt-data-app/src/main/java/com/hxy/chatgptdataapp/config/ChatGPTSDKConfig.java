package com.hxy.chatgptdataapp.config;

import com.hxy.chatgptsdkjavalearning.session.OpenAiSession;
import com.hxy.chatgptsdkjavalearning.session.OpenAiSessionFactory;
import com.hxy.chatgptsdkjavalearning.session.defaults.DefaultOpenAiSessionFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 工厂配置开启
 */
@Configuration
@EnableConfigurationProperties(ChatGPTSDKConfigProperties.class)
public class ChatGPTSDKConfig {
    @Bean
    public OpenAiSession openAiSession(ChatGPTSDKConfigProperties properties){
        // 1.配置文件
        com.hxy.chatgptsdkjavalearning.session.Configuration configuration = new com.hxy.chatgptsdkjavalearning.session.Configuration();
        configuration.setApiHost(properties.getApiHost());
        configuration.setApiKey(properties.getApiKey());
        configuration.setAuthToken(properties.getAutoToken());

        // 2.会话工厂
        OpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(configuration);

        // 3.开启会话
        return factory.openSession();
    }

}
