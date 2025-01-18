package com.hxy.chatgptdatainfrastructure.dao;

import com.hxy.chatgptdatainfrastructure.po.OpenAIProductPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品Dao
 */
@Mapper
public interface IOpenAIProductDao {
    OpenAIProductPO queryProductByProductId(Integer productId);
}
