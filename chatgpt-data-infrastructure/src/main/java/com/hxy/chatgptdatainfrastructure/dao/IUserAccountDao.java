package com.hxy.chatgptdatainfrastructure.dao;

import com.hxy.chatgptdatainfrastructure.po.UserAccountPO;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户账户DAO
 */
@Mapper
public interface IUserAccountDao {
    int subAccountQuota(String openid);

    UserAccountPO queryUserAccount(String openid);

    void insert(UserAccountPO userAccountPO);

    int addAccountQuota(UserAccountPO userAccountPOReq);
}
