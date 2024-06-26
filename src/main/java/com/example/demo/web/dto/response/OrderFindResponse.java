package com.example.demo.web.dto.response;

import com.example.demo.web.domain.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderFindResponse {
    private String orderName;
    private String orderNo;
    private String impUid;
    private String choosingOption;

    private int orderAmount;
    private int discountAmount;
    private int paymentAmount;
    public OrderFindResponse(Orders orders) {
        orderName = orders.getOrderName();
        orderNo = orders.getOrderNo();
        impUid = orders.getImpUid();
        choosingOption = orders.getChoosingOption();
        orderAmount = orders.getOrderAmount();
        discountAmount = orders.getDiscountAmount();
        paymentAmount = orders.getPaymentAmount();
    }

}