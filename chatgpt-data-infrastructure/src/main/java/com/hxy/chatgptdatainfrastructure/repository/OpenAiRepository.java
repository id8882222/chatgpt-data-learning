package com.hxy.chatgptdatainfrastructure.repository;

import com.hxy.chatgptdatadomain.openai.model.entity.UserAccountQuotaEntity;
import com.hxy.chatgptdatadomain.openai.model.valobj.UserAccountStatusVO;
import com.hxy.chatgptdatadomain.repository.IOpenAiRepository;
import com.hxy.chatgptdatainfrastructure.dao.IUserAccountDao;
import com.hxy.chatgptdatainfrastructure.po.UserAccountPO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 *  OpenAi 仓储服务
 */
@Repository
public class OpenAiRepository implements IOpenAiRepository {

    @Resource
    private IUserAccountDao userAccountDao;

    @Override
    public int subAccountQuota(String openai) {
        return userAccountDao.subAccountQuota(openai);
    }

    @Override
    public UserAccountQuotaEntity queryUserAccount(String openid) {
        UserAccountPO userAccountPO = userAccountDao.queryUserAccount(openid);
        if(null == userAccountPO) return null;
        UserAccountQuotaEntity userAccountQuotaEntity = new UserAccountQuotaEntity();
        userAccountQuotaEntity.setOpenid(userAccountPO.getOpenid());
        userAccountQuotaEntity.setTotalQuota(userAccountPO.getTotalQuota());
        userAccountQuotaEntity.setSurplusQuota(userAccountPO.getSurplusQuota());
        userAccountQuotaEntity.setUserAccountStatusVO(UserAccountStatusVO.get(userAccountPO.getStatus()));
        return userAccountQuotaEntity;
    }
}
