package com.hxy.chatgptdatadomain.openai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hxy.chatgptdatadomain.openai.model.aggregates.ChatProcessAggregate;
import com.hxy.chatgptdatadomain.openai.model.entity.RuleLogicEntity;
import com.hxy.chatgptdatadomain.openai.model.entity.UserAccountQuotaEntity;
import com.hxy.chatgptdatadomain.openai.service.rule.factory.DefaultLogicFactory;
import com.hxy.chatgptdatadomain.repository.IOpenAiRepository;
import com.hxy.chatgptdatatypes.common.Constants;
import com.hxy.chatgptdatatypes.exception.ChatGPTException;
import com.hxy.chatgptsdkjavalearning.session.OpenAiSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;

@Slf4j
public abstract class AbstractChatService implements IChatService{

    @Resource
    protected OpenAiSession openAiSession;

    @Resource
    IOpenAiRepository openAiRepository;

    @Override
    public ResponseBodyEmitter completions(ResponseBodyEmitter emitter, ChatProcessAggregate chatProcess) {
        try{
            //1.请求应答
            // 当 ResponseBodyEmitter 完成（即 emitter.complete() 被调用）时触发emitter.onCompletion。
            emitter.onCompletion(() -> {
                log.info("流式问答请求完成，使用模型：{}", chatProcess.getModel());
            });
            emitter.onError(throwable -> {
                log.error("流式问答请求疫情，使用模型：{}", chatProcess.getModel(), throwable);
            });
            // 账户获取
            UserAccountQuotaEntity userAccountQuotaEntity = openAiRepository.queryUserAccount(chatProcess.getOpenid());

            log.info("userAccountQuotaEntity == null ? {}", userAccountQuotaEntity == null);

            // 规则过滤
            RuleLogicEntity<ChatProcessAggregate> ruleLogicEntity = this.doCheckLogic(chatProcess, userAccountQuotaEntity,
//                    DefaultLogicFactory.LogicModel.ACCESS_LIMIT.getCode(),
//                    DefaultLogicFactory.LogicModel.SENSITIVE_WORD.getCode(),
                    null != userAccountQuotaEntity ? DefaultLogicFactory.LogicModel.ACCOUNT_STATUS.getCode() : DefaultLogicFactory.LogicModel.NULL.getCode(),
//                    null != userAccountQuotaEntity ? DefaultLogicFactory.LogicModel.MODEL_TYPE.getCode() : DefaultLogicFactory.LogicModel.NULL.getCode(),
                    null != userAccountQuotaEntity ? DefaultLogicFactory.LogicModel.USER_QUOTA.getCode() : DefaultLogicFactory.LogicModel.NULL.getCode());
            // 4. 应答处理【chatGPT、chatGLM策略模式】
            this.doMessageResponse(chatProcess, emitter);
        }catch (Exception e){
            throw new ChatGPTException(Constants.ResponseCode.UN_ERROR.getCode(), Constants.ResponseCode.UN_ERROR.getInfo());
        }
        // 5. 返回结果
        return emitter;
    }
    protected abstract void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter responseBodyEmitter) throws JsonProcessingException;

    protected abstract RuleLogicEntity<ChatProcessAggregate> doCheckLogic(ChatProcessAggregate chatProcess, UserAccountQuotaEntity userAccountQuotaEntity, String ...logics) throws Exception;
}
