package com.neuedu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.IProductService;
import com.neuedu.utils.DateUtils;
import com.neuedu.utils.FTPUtil;
import com.neuedu.utils.ProperitiesUtils;
import com.neuedu.vo.ProductDetailVo;
import com.neuedu.vo.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    ICategoryService categoryService;

    @Override
    public ServerResponse addOrUpdate(Product product) {

        //1、参数非空校验
        if (product==null){
            return ServerResponse.serverResponseByError("参数不能为空");
        }

        //2、上传主图------1.jsp,2.jsp,3.jsp(第一张设为主图)----字符串隔开
        String subImages=product.getSubImages();
        if (subImages!=null  && !subImages.equals("")){
            String [] subImageArr=subImages.split(",");
            if (subImageArr.length>0){
                //设置商品的主图
                product.setMainImage(subImageArr[0]);
            }
        }

        //3、添加或更新
        if (product.getId()==null){
            //添加
            int result=productMapper.insert(product);
            if (result>0){
                return ServerResponse.serverResponseBySuccess();
            }else {
                return ServerResponse.serverResponseByError("添加失败");
            }
        }else {
            //更新
            int result=productMapper.updateByPrimaryKey(product);
            if (result>0){
                return ServerResponse.serverResponseBySuccess();
            }else {
                return ServerResponse.serverResponseByError("更新失败");
            }
        }

        //4、返回结果

    }

    @Override
    public ServerResponse set_sale_status(Integer productId, Integer status) {
        //1、参数非空校验
        if (productId==null){
            return ServerResponse.serverResponseByError("商品id参数不能为空");
        }
        if (status ==null){
            return ServerResponse.serverResponseByError("商品状态参数不能为空");
        }
        //2、更新商品的状态
        Product product=new Product();
        product.setId(productId);
        product.setStatus(status);
        int result=productMapper.updateProductStatus(product);
        //3、返回结果
        if (result>0){
            return ServerResponse.serverResponseBySuccess();
        }else {
            return ServerResponse.serverResponseByError("更新失败");
        }

    }

    @Override
    public ServerResponse details(Integer productId) {

        //1、参数非空校验
        if (productId==null){
            return ServerResponse.serverResponseByError("商品id参数不能为空");
        }
        //2、根据商品的id查询商品
        Product product =productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.serverResponseByError("商品不存在");
        }
        //3、product----productDetailVo
        ProductDetailVo productDetailVo=assembleProductDetailVO(product);
        //4、返回结果
        return ServerResponse.serverResponseBySuccess(productDetailVo);
    }
    private ProductDetailVo assembleProductDetailVO(Product product){
            ProductDetailVo productDetailVo=new ProductDetailVo();
            productDetailVo.setCategoryId(product.getCategoryId());
            productDetailVo.setCreateTime(DateUtils.dateToStr(product.getCreateTime()));
            productDetailVo.setDetail(product.getDetail());
            productDetailVo.setImageHost(ProperitiesUtils.readByKey("imageHost"));
            productDetailVo.setName(product.getName());
            productDetailVo.setMainImage(product.getMainImage());
            productDetailVo.setId(product.getId());
            productDetailVo.setPrice(product.getPrice());
            productDetailVo.setStatus(product.getStatus());
            productDetailVo.setStock(product.getStock());
            productDetailVo.setSubImage(product.getSubImages());
            productDetailVo.setSubtitle(product.getSubtitle());
            productDetailVo.setUpdateTime(DateUtils.dateToStr(product.getUpdateTime()));
            Category category=categoryMapper.selectByPrimaryKey(product.getCategoryId());
            if (category!=null){
                productDetailVo.setParentCategoryId(category.getParentId());;
            }else {
                productDetailVo.setParentCategoryId(0);
            }

        return productDetailVo;
    }


    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        //1、查询商品数据  select * from product limit (pageNum-1)*pageSize,pageSize
        List<Product> productList=productMapper.selectAll();
        List<ProductListVO> productListVOList=Lists.newArrayList();
        if (productList!=null && productList.size()>0){
            for (Product product:productList){
                ProductListVO productListVO=assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo=new PageInfo(productListVOList);

        return ServerResponse.serverResponseBySuccess(pageInfo);
    }

    private ProductListVO assembleProductListVO(Product product){
        ProductListVO productListVO=new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setName(product.getName());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());

        return productListVO;
    }

    @Override
    public ServerResponse search(Integer productId, String productName, Integer pageNum, Integer pageSize) {

        //select * from product where productId ? and productName like %name%
        PageHelper.startPage(pageNum,pageSize);

        if (productName!=null && !productName.equals("")){
            productName="%"+productName+"%";
        }
        List<Product> productList=productMapper.findProductByProductIdAndProductName(productId,productName);
        List<ProductListVO> productListVOList=Lists.newArrayList();
        if (productList!=null && productList.size()>0){
            for (Product product:productList){
                ProductListVO productListVO=assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo=new PageInfo(productListVOList);
        return ServerResponse.serverResponseBySuccess(pageInfo);
    }

    @Override
    public ServerResponse upload(MultipartFile file, String path) {

        if (file==null){
            return ServerResponse.serverResponseByError();
        }

        //1、获取图片的名字
        String originalFilename=file.getOriginalFilename();
        //获取图片的扩展名
        String exName=originalFilename.substring(originalFilename.lastIndexOf("."));//.jpg
        //生成新的唯一的名字
        String newFileName=UUID.randomUUID().toString()+exName;

        File pathFile=new File(path);
        if (pathFile.exists()){
            pathFile.mkdirs();
        }

        File file1=new File(path,newFileName);
        try {
            file.transferTo(file1);
            //上传到图片服务器
            FTPUtil.uploadfile(Lists.<File>newArrayList(file1));


            Map<String,String> map=Maps.newHashMap();
            map.put("uri",newFileName);
            map.put("url",ProperitiesUtils.readByKey("imageHost")+"/"+newFileName);
            //删除应用服务器上的图片
            file1.delete();

            return ServerResponse.serverResponseBySuccess(map);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * 前台接口--商品详情
     * */
    @Override
    public ServerResponse details_protal(Integer productId) {

        //1、参数校验
        if (productId==null){
            return ServerResponse.serverResponseByError("商品id参数不能为空");
        }
        //2、查询product
        Product product =productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.serverResponseByError("商品不存在");
        }

        //3、校验商品的状态
        if (product.getStatus()!= Const.ProductStatusEum.PRODUCT_ONLINE.getCode()){
            return ServerResponse.serverResponseByError("商品已下架或删除");
        }
        //4、获取productDetailsVO
        ProductDetailVo productDetailVo=assembleProductDetailVO(product);
        //5、返回结果
        return ServerResponse.serverResponseBySuccess(productDetailVo);
    }

    @Override
    public ServerResponse list_protal(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {

        //1.参数校验-->categoryId和keyword不能同时为空
        if (categoryId==null && (keyword==null||keyword.equals("")) ){
            return ServerResponse.serverResponseByError("参数错误");
        }
        //2.根据categoryId查询类别
        Set<Integer> integerSet= Sets.newHashSet();
        if (categoryId!=null){
            Category category=categoryMapper.selectByPrimaryKey(categoryId);
            //该categoryId没有商品
            if (category==null && (keyword==null||keyword.equals("")) ){
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVO> productListVOList=Lists.newArrayList();
                PageInfo pageInfo=new PageInfo(productListVOList);
                return  ServerResponse.serverResponseBySuccess(pageInfo);
            }
            //查询出categoryId下所有子类
            ServerResponse serverResponse=categoryService.get_deep_category(categoryId);
            //问题：获取到的只是1的子类，没有1   可以在这里添加本类


            if (serverResponse.isSuccess()){
                integerSet= (Set<Integer>) serverResponse.getData();
            }
        }
        //3.根据keyword查询类别
        if (keyword!=null && !keyword.equals("")){
            keyword="%"+keyword+"%";
        }
        if (orderBy.equals("")){
            PageHelper.startPage(pageNum,pageSize);
        }else{
            String[] orderByArr=orderBy.split("_");
            if (orderByArr.length>1){
                PageHelper.startPage(pageNum,pageSize,orderByArr[0]+" "+orderByArr[1]);
            }else{
                PageHelper.startPage(pageNum,pageSize);
            }
        }
        //4.List<Product>-->List<ProductListVO>
        List<Product> productList=productMapper.searchProduct(integerSet,keyword);
        List<ProductListVO> productListVOList=Lists.newArrayList();
        if (productList!=null && productList.size()>0){
            for (Product product:productList){
                ProductListVO productListVO=assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        //5.分页
        PageInfo pageInfo=new PageInfo(productListVOList);
        //6.返回结果
        return ServerResponse.serverResponseBySuccess(pageInfo);

    }
}
