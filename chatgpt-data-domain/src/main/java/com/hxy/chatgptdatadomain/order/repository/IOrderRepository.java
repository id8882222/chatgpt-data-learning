package com.hxy.chatgptdatadomain.order.repository;

import com.hxy.chatgptdatadomain.order.model.aggregates.CreateOrderAggregate;
import com.hxy.chatgptdatadomain.order.model.entity.PayOrderEntity;
import com.hxy.chatgptdatadomain.order.model.entity.ProductEntity;
import com.hxy.chatgptdatadomain.order.model.entity.ShopCartEntity;
import com.hxy.chatgptdatadomain.order.model.entity.UnpaidOrderEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单仓储接口
 */
public interface IOrderRepository {
    UnpaidOrderEntity queryUnpaidOrder(ShopCartEntity shopCartEntity);

    ProductEntity queryProduct(Integer productId);

    void saveOrder(CreateOrderAggregate aggregate);

    void updateOrderPayInfo(PayOrderEntity payOrderEntity);

    boolean changeOrderPaySuccess(String orderId, String transactionId, BigDecimal totoalAmount, Date payTime);

    CreateOrderAggregate queryOrder(String orderId);

    void deliverGoods(String orderId);

    List<String> queryReplenishmentOrder();

    List<String> queryNoPayNotifyOrder();

    List<String> queryTimeoutClosedOrderList();

    boolean changeOrderClose(String orderId);

    List<ProductEntity> queryProductList();
}
