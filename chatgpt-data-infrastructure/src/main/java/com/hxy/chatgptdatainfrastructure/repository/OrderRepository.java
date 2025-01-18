package com.hxy.chatgptdatainfrastructure.repository;

import cn.hutool.db.sql.Order;
import com.hxy.chatgptdatadomain.order.model.aggregates.CreateOrderAggregate;
import com.hxy.chatgptdatadomain.order.model.entity.*;
import com.hxy.chatgptdatadomain.order.model.valobj.PayStatusVO;
import com.hxy.chatgptdatadomain.order.repository.IOrderRepository;
import com.hxy.chatgptdatainfrastructure.dao.IOpenAIOrderDao;
import com.hxy.chatgptdatainfrastructure.dao.IOpenAIProductDao;
import com.hxy.chatgptdatainfrastructure.dao.IUserAccountDao;
import com.hxy.chatgptdatainfrastructure.po.OpenAIOrderPO;
import com.hxy.chatgptdatainfrastructure.po.OpenAIProductPO;
import com.hxy.chatgptdatainfrastructure.po.UserAccountPO;
import com.hxy.chatgptdatatypes.enums.OpenAIProductEnableModel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单仓储服务
 */
@Repository
public class OrderRepository implements IOrderRepository {
    @Resource
    private IOpenAIOrderDao openAIOrderDao;

    @Resource
    private IOpenAIProductDao openAIProductDao;

    @Resource
    private IUserAccountDao userAccountDao;

    @Override
    public UnpaidOrderEntity queryUnpaidOrder(ShopCartEntity shopCartEntity) {
        OpenAIOrderPO openAIOrderPOReq = new OpenAIOrderPO();
        openAIOrderPOReq.setOrderId(shopCartEntity.getOpenid());
        openAIOrderPOReq.setProductId(shopCartEntity.getProductId());
        OpenAIOrderPO openAIOrderPORes  = openAIOrderDao.queryUnpaidOrder(openAIOrderPOReq);
        if(null == openAIOrderPORes) return null;
        return UnpaidOrderEntity.builder()
                .openid(shopCartEntity.getOpenid())
                .orderId(openAIOrderPORes.getOrderId())
                .productName(openAIOrderPORes.getProductName())
                .totalAmount(openAIOrderPORes.getTotalAmount())
                .payUrl(openAIOrderPORes.getPayUrl())
                .payStatus(PayStatusVO.get(openAIOrderPORes.getPayStatus()))
                .build();
    }

    @Override
    public ProductEntity queryProduct(Integer productId) {
        OpenAIProductPO openAIProductPO = openAIProductDao.queryProductByProductId(productId);
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(openAIProductPO.getProductId());
        productEntity.setProductName(openAIProductPO.getProductName());
        productEntity.setProductDesc(openAIProductPO.getProductDesc());
        productEntity.setQuota(openAIProductPO.getQuota());
        productEntity.setPrice(openAIProductPO.getPrice());
        productEntity.setEnable(OpenAIProductEnableModel.get(openAIProductPO.getIsEnabled()));
        return productEntity;
    }

    @Override
    public void saveOrder(CreateOrderAggregate aggregate) {
        String openid = aggregate.getOpenid();
        ProductEntity product = aggregate.getProduct();
        OrderEntity order = aggregate.getOrder();
        OpenAIOrderPO openAIOrderPO = new OpenAIOrderPO();
        openAIOrderPO.setOpenid(openid);
        openAIOrderPO.setProductId(product.getProductId());
        openAIOrderPO.setProductName(product.getProductName());
        openAIOrderPO.setProductQuota(product.getQuota());
        openAIOrderPO.setOrderId(order.getOrderId());
        openAIOrderPO.setOrderTime(order.getOrderTime());
        openAIOrderPO.setOrderStatus(order.getOrderStatus().getCode());
        openAIOrderPO.setTotalAmount(order.getTotalAmount());
        openAIOrderPO.setPayType(order.getPayTypeVO().getCode());
        openAIOrderPO.setPayStatus(PayStatusVO.WAIT.getCode());
        openAIOrderDao.insert(openAIOrderPO);
    }

    @Override
    public void updateOrderPayInfo(PayOrderEntity payOrderEntity) {
        OpenAIOrderPO openAIOrderPO = new OpenAIOrderPO();
        openAIOrderPO.setOpenid(payOrderEntity.getOpenid());
        openAIOrderPO.setOrderId(payOrderEntity.getOrderId());
        openAIOrderPO.setPayUrl(payOrderEntity.getPayUrl());
        openAIOrderPO.setPayStatus(payOrderEntity.getPayStatus().getCode());
        openAIOrderDao.updateOrderPayInfo(openAIOrderPO);
    }

    @Override
    public boolean changeOrderPaySuccess(String orderId, String transactionId, BigDecimal totalAmount, Date payTime) {
        OpenAIOrderPO openAIOrderPO = new OpenAIOrderPO();
        openAIOrderPO.setOrderId(orderId);
        openAIOrderPO.setPayAmount(totalAmount);
        openAIOrderPO.setPayTime(payTime);
        openAIOrderPO.setTransactionId(transactionId);
        int count = openAIOrderDao.changeOrderPaySuccess(openAIOrderPO);
        return count == 1;
    }

    @Override
    public CreateOrderAggregate queryOrder(String orderId) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 350, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void deliverGoods(String orderId) {
        OpenAIOrderPO openAIOrderPO = openAIOrderDao.queryOrder(orderId);
        //1.变更发货状态
        int updateOrderStatusDeliverGoodsCount = openAIOrderDao.updateOrderStatusDeliverGoods(orderId);
        if (1 != updateOrderStatusDeliverGoodsCount) throw new RuntimeException("updateOrderStatusDeliverGoodsCount update count is not equal 1");
        //2.账户额度变更
        UserAccountPO userAccountPO = userAccountDao.queryUserAccount(openAIOrderPO.getOpenid());
        UserAccountPO userAccountPOReq = new UserAccountPO();
        userAccountPOReq.setOpenid(openAIOrderPO.getOpenid());
        userAccountPOReq.setTotalQuota(openAIOrderPO.getProductQuota());
        userAccountPOReq.setSurplusQuota(openAIOrderPO.getProductQuota());
        if(null != userAccountPO){
            int addAccountQuotaCount = userAccountDao.addAccountQuota(userAccountPOReq);
            if (1 != addAccountQuotaCount) throw new RuntimeException("addAccountQuotaCount update count is not equal 1");
        }else{
            userAccountDao.insert(userAccountPOReq);
        }
    }

    @Override
    public List<String> queryReplenishmentOrder() {
        return null;
    }

    @Override
    public List<String> queryNoPayNotifyOrder() {
        return openAIOrderDao.queryNoPayNotifyOrder();
    }

    @Override
    public List<String> queryTimeoutClosedOrderList() {
        return openAIOrderDao.queryTimeoutCloseOrderList();
    }

    @Override
    public boolean changeOrderClose(String orderId) {
        return false;
    }

    @Override
    public List<ProductEntity> queryProductList() {
        return null;
    }
}
