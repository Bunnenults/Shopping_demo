package com.neuedu.controller.backend;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
@RestController
@RequestMapping(value = "/manager/order")
public class OrderManagerController {

    @Autowired
    IOrderService orderService;

    /**
     * 订单发货
     * */
    @RequestMapping(value = "/send/{orderNo}")
    public ServerResponse send(HttpSession session, @PathVariable("orderNo") Long orderNo){

            return orderService.send(orderNo);

    }



}
