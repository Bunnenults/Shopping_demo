package com.neuedu.controller.backend;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manager/product")
public class ProductManagerController {

    @Autowired
    IProductService productService;
    /**
     * 新增或者更新商品
     * */
    @RequestMapping(value="/addOrUpdate.do")
    public ServerResponse addOrUpdate(HttpSession session, Product product){


        return productService.addOrUpdate(product);
    }

    /**
     * 商品上下架
     * */
    @RequestMapping(value = "/set_sale_status/{productId}/{status}")
    public ServerResponse set_sale_status(HttpSession session, @PathVariable("productId") Integer productId,
                                          @PathVariable("status") Integer status){


        return productService.set_sale_status(productId,status);
    }



    /**
     * 查看商品详情
     * */
    @RequestMapping(value = "/details/{productId}")
    public ServerResponse details(HttpSession session,@PathVariable("productId") Integer productId){



        return productService.details(productId);
    }

    /**
     * 查看商品列表
     * */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                               @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){


        return productService.list(pageNum,pageSize);
    }

    /**
     * 商品搜索
     * */
    @RequestMapping(value = "/search.do")
    public ServerResponse search(HttpSession session,
                                 @RequestParam(value = "productId",required = false)Integer productId,
                                 @RequestParam(value = "productName",required = false)String productName,
                                 @RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                                 @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){


        return productService.search(productId,productName,pageNum,pageSize);
    }



}
