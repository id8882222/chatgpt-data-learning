package com.hxy.chatgptdatadomain.order.model.aggregates;

import com.hxy.chatgptdatadomain.order.model.entity.OrderEntity;
import com.hxy.chatgptdatadomain.order.model.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderAggregate {
    /** 用户ID；微信用户唯一标识 */
    private String openid;
    /** 商品 */
    private ProductEntity product;
    /** 订单 */
    private OrderEntity order;
}
