package com.neuedu.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车实体类CartVO
 * */
public class CartVO {

    //购物信息集合
    private List<CartProductVO> cartProductVOList;
    //购物车是否全选
    private boolean isallchecked;
    //总价格
    private BigDecimal carttotalprice;

    public List<CartProductVO> getCartProductVOList() {
        return cartProductVOList;
    }

    public void setCartProductVOList(List<CartProductVO> cartProductVOList) {
        this.cartProductVOList = cartProductVOList;
    }

    public boolean isIsallchecked() {
        return isallchecked;
    }

    public void setIsallchecked(boolean isallchecked) {
        this.isallchecked = isallchecked;
    }

    public BigDecimal getCarttotalprice() {
        return carttotalprice;
    }

    public void setCarttotalprice(BigDecimal carttotalprice) {
        this.carttotalprice = carttotalprice;
    }
}
