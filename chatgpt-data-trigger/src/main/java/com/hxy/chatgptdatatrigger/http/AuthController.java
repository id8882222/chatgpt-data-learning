package com.hxy.chatgptdatatrigger.http;

import com.alibaba.fastjson.JSON;
import com.hxy.chatgptdatadomain.auth.model.entity.AuthStateEntity;
import com.hxy.chatgptdatadomain.auth.model.valobj.AuthTypeVO;
import com.hxy.chatgptdatadomain.auth.service.IAuthService;
import com.hxy.chatgptdatatypes.common.Constants;
import com.hxy.chatgptdatatypes.model.Response;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 鉴权登录
 */
@Slf4j
@RestController()
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/auth/")
public class AuthController {
    @Resource
    private IAuthService authService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Response<String> doLogin(@RequestParam String code){
        log.info("鉴权登录校验开始，验证码: {}", code);
        try{
            AuthStateEntity authStateEntity = authService.doLogin(code);
            log.info("鉴权登录校验完成，验证码: {} 结果: {}", code, JSON.toJSONString(authStateEntity));
            // 拦截，鉴权失败
            if(!AuthTypeVO.A0000.getCode().equals(authStateEntity.getCode())){
                return Response.<String>builder()
                        .code(Constants.ResponseCode.TOKEN_ERROR.getCode())
                        .info(Constants.ResponseCode.TOKEN_ERROR.getInfo())
                        .build();
            }
            // 放行，鉴权成功
            return Response.<String>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .data(authStateEntity.getToken())
                    .build();
        }catch (Exception e){
            log.error("鉴权登录校验失败，验证码: {}", code);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
