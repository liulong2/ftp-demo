package com.xybbz.config.demo;

import lombok.Data;

@Data
public class OrderDetail {
    private Long orderid;
    private String orderPrice;
    private String orderSku;
}
