package com.hxy.chatgptdatadomain.weixin.service.validate;

import com.hxy.chatgptdatadomain.weixin.service.IWeiXinValidateService;
import com.hxy.chatgptdatatypes.sdk.weixin.SignatureUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 验签接口实现
 */
@Service
public class WeiXinValidateService implements IWeiXinValidateService {
    @Value("${wx.config.token}")
    private String token;
    @Override
    public boolean checkSign(String signature, String timestamp, String nonce) {
        return SignatureUtil.check(token, signature, timestamp, nonce);
    }
}
