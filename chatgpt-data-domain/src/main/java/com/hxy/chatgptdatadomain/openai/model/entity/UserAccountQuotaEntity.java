package com.hxy.chatgptdatadomain.openai.model.entity;

import com.hxy.chatgptdatadomain.openai.model.valobj.UserAccountStatusVO;
import com.hxy.chatgptdatatypes.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *  * @description 用户账户额度实体对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountQuotaEntity {
    /**
     * 用户ID
     */
    private String openid;
    /**
     * 总量额度
     */
    private Integer totalQuota;
    /**
     * 剩余额度
     */
    private Integer surplusQuota;
    /**
     * 账户状态
     */
    private UserAccountStatusVO userAccountStatusVO;


}
