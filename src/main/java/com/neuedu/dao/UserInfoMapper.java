package com.neuedu.dao;

import com.neuedu.pojo.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface UserInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_user
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_user
     *
     * @mbg.generated
     */
    int insert(UserInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_user
     *
     * @mbg.generated
     */
    UserInfo selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_user
     *
     * @mbg.generated
     */
    List<UserInfo> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_user
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(UserInfo record);


    /**
     *  校验用户名是否存在
     * */
    int checkUsername(String username);


    /**
     *  校验邮箱是否存在
     * */
    int checkEmail(String email);

    /**
     * 根据用户名和密码查询
     * dao接口一个参数不需要加注解，多个参数要加注解
     * */
    UserInfo selectUserInfoByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    /**
     * 根据用户名查询密码问题
     * */
    String selectQuestionByUsername(String username);


    /**
     * 根据用户名和密保问题及答案查询
     * */
    int selectByUsernameAndQuestionAndAnswer(@Param("username")String username,
                                             @Param("question")String question,
                                             @Param("answer")String answer);

    /**
     * 修改用户密码接口
     * */
    int updateUserPassword(@Param("username")String username,
                           @Param("password")String newPassword);


    /**
     * 更新用户信息
     * */
    int updateUserBySelectActive(UserInfo userInfo);


    /**
     * 更新用户token信息
     * */
    int updateTokenByUserId(@Param("userId") Integer userId,
                            @Param("token") String token);


    /***
     *
     * 根据token查询用户信息
     */
    UserInfo findUserInfoByToken(String token);

}