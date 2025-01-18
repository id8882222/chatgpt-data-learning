package com.hxy.chatgptdatainfrastructure.dao;

import com.hxy.chatgptdatainfrastructure.po.OpenAIOrderPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 订单Dao
 */
@Mapper
public interface IOpenAIOrderDao {
    OpenAIOrderPO queryUnpaidOrder(OpenAIOrderPO openAIOrderPOReq);

    void insert(OpenAIOrderPO order);

    void updateOrderPayInfo(OpenAIOrderPO openAIOrderPO);

    int changeOrderPaySuccess(OpenAIOrderPO openAIOrderPO);

    OpenAIOrderPO queryOrder(String orderId);

    int updateOrderStatusDeliverGoods(String orderId);

    List<String> queryNoPayNotifyOrder();

    List<String> queryTimeoutCloseOrderList();
}
