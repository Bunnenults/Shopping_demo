package com.neuedu.controller.portal;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    IProductService productService;

    /**
     * 商品详情
     * */
    @RequestMapping(value = "/details/productId/{productId}")
    public ServerResponse details(@PathVariable("productId") Integer productId){

        return productService.details_protal(productId);
    }

    /**
     * 前台--搜索商品并且排序
     * */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(@RequestParam(value = "categoryId",required = false)Integer categoryId,
                               @RequestParam(value = "keyword",required = false)String keyword,
                               @RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                               @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "orderBy",required = false,defaultValue = "")String orderBy){


            return productService.list_protal(categoryId,keyword,pageNum,pageSize,orderBy);
    }


}
