package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface IProductService {

    /**
     * 新增或者更新商品
     * */
    ServerResponse addOrUpdate(Product product);

    /**
     * 商品上下架
     * */
    ServerResponse set_sale_status(Integer productId,Integer status);


    /**
     * 后台--查看商品详情
     * */
    ServerResponse details(Integer productId);


    /**
     * 后台--商品列表，分页
     * */
    ServerResponse list(Integer pageNum,Integer pageSize);


    /**
     * 后台--搜索商品
     * */
    ServerResponse search(Integer productId,String productName,Integer pageNum,Integer pageSize);


    /**
     * 图片上传
     * */
    ServerResponse upload(MultipartFile file,String path);

    /**
     * 前台--商品详情
     * */
    ServerResponse details_protal(Integer productId);

    /**
     * 前台---商品搜索
     * */
    ServerResponse list_protal(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);

}
