package com.xybbz.config.demo;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class test {
    public static void main(String[] args) {
        Order order = new Order();
        order.setId(1L);
        order.setName("order1");
        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderid(1L);
        orderDetail.setOrderPrice("1USD");
        orderDetail.setOrderSku("Sku1");

        orderDetailList.add(orderDetail);

        OrderDetail orderDetail2 = new OrderDetail();
        orderDetail2.setOrderid(1L);
        orderDetail2.setOrderPrice("2USD");
        orderDetail2.setOrderSku("Sku2");
        orderDetailList.add(orderDetail2);

        try {
            HashMap<String, Class<?>> addMap = new HashMap<>();
            HashMap<String,Object> addValMap = new HashMap<>();
            addMap.put("orderDetail", Class.forName("java.util.List"));
            addValMap.put("orderDetail", orderDetailList);
            Object obj2= new ClassUtil().dynamicClass(order,addMap,addValMap);

            System.out.println(JSON.toJSONString(obj2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
