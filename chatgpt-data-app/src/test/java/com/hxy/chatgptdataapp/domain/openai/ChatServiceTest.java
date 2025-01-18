package com.hxy.chatgptdataapp.domain.openai;

import com.hxy.chatgptdatadomain.openai.model.aggregates.ChatProcessAggregate;
import com.hxy.chatgptdatadomain.openai.model.entity.MessageEntity;
import com.hxy.chatgptdatadomain.openai.service.IChatService;

import com.hxy.chatgptsdkjavalearning.common.Constants;
import com.hxy.chatgptsdkjavalearning.domain.chat.ChatCompletionRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ChatServiceTest {
    @Resource
    private IChatService chatService;

    @Test
    public void test_completions() throws InterruptedException{
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();

        ChatProcessAggregate chatProcessAggregate = new ChatProcessAggregate();
        chatProcessAggregate.setOpenid("xfg");
        chatProcessAggregate.setModel(ChatCompletionRequest.Model.GPT_3_5_TURBO.getCode());
        chatProcessAggregate.setMessages(Collections.singletonList(MessageEntity.builder().role(Constants.Role.USER.getCode()).content("1+1").build()));
        ResponseBodyEmitter completions = chatService.completions(emitter, chatProcessAggregate);

        new CountDownLatch(1).await();
    }
}
