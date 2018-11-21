package com.neuedu.service;

import com.neuedu.common.ServerResponse;



public interface ICartService {

    //添加到购物车
    public ServerResponse add(Integer userId,Integer productId,Integer count);


    //购物车列表
     ServerResponse list(Integer userId);


     //更新购物车中某个商品的数量
    ServerResponse update(Integer userId,Integer productId,Integer count);

    //移除购物车中的么某个或某些商品
    ServerResponse delete_product(Integer userId,String productIds);

    //选中购物车中的商品
    ServerResponse select(Integer userId,Integer productId,Integer check);

    //查询购物车中产品数量
    ServerResponse get_cart_product_count(Integer userId);
}
