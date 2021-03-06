package com.neuedu.controller.backend;


import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manager/category")
public class CategoryManagerController {

    @Autowired
    ICategoryService categoryService;
    /**
     * 获取品类的子节点
     * */
    @RequestMapping("/get_category/{categoryId}")
    public ServerResponse get_category(HttpSession session,
                                       @PathVariable("categoryId") Integer categoryId){

        return categoryService.get_category(categoryId);
    }


    /**
     * 增加节点（平级）
     * */
    @RequestMapping("/add_category/{parentId}/{categoryName}")
    public ServerResponse add_category(HttpSession session,@PathVariable("parentId") Integer parentId,
                                       @PathVariable("categoryName") String categoryName){

        return categoryService.add_category(parentId,categoryName);
    }

    /**
     * 修改节点
     * */
    @RequestMapping("/set_category_name/{categoryId}/{categoryName}")
    public ServerResponse set_category_name(HttpSession session,@PathVariable("categoryId") Integer categoryId,
                                            @PathVariable("categoryName") String categoryName){


        return categoryService.set_category_name(categoryId,categoryName);
    }



    /**
     * 获取当前分类id及递归子节点categoryId
     * */
    @RequestMapping("/get_deep_category/{categoryId}")
    public ServerResponse get_deep_category(HttpSession session,@PathVariable("categoryId") Integer categoryId){

        //int count=3/0;

        return categoryService.get_deep_category(categoryId);
    }


}
