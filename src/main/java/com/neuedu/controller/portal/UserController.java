package com.neuedu.controller.portal;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import com.neuedu.utils.IpUtils;
import com.neuedu.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.SocketException;
import java.net.UnknownHostException;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    IUserService userService;
    /**
     * 登录
     * */
    @RequestMapping(value = "/login/{username}/{password}")
    public ServerResponse login(HttpServletRequest request, HttpServletResponse response,HttpSession session, @PathVariable("username") String username,
                                @PathVariable("password") String password) {
        ServerResponse serverResponse=userService.login(username,password);
        if(serverResponse.isSuccess()){//登录成功
            UserInfo userInfo= (UserInfo) serverResponse.getData();
            if (userInfo.getRole()==Const.RoleEnum.ROLE_ADMIN.getCode()){
                return ServerResponse.serverResponseByError("无权限操作");
            }
            session.setAttribute(Const.CURRENTUSER,userInfo);

            //生成autoLogintoken
            String ip=IpUtils.getRemoteAddress(request);

            try {
                String mac = IpUtils.getMACAddress(ip);
                String token=MD5Utils.getMD5Code(mac);
                System.out.println(token);
                //token保存到数据库
                userService.updateTokenByUserId(userInfo.getId(),token);
                //token作为cookie响应到客户端
                Cookie autoLoginTokenCookie=new Cookie(Const.AUTOLOGINTOKEN,token);
                //autoLoginTokenCookie.setPath("/");
                autoLoginTokenCookie.setMaxAge(60*60*24*7);//7天
                response.addCookie(autoLoginTokenCookie);



            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SocketException e) {
                e.printStackTrace();
            }

        }

        return serverResponse;
    }

    /**
     * 注册
     * */
    @RequestMapping(value = "/register.do")
    public ServerResponse register(HttpSession session,UserInfo userInfo) {
        ServerResponse serverResponse=userService.register(userInfo);


        return serverResponse;
    }

    /**
     * 根据用户名查询密保问题
     * */
    @RequestMapping(value = "/forget_get_question/{username}")
    public ServerResponse forget_get_question(@PathVariable("username") String username) {
        ServerResponse serverResponse=userService.forget_get_question(username);


        return serverResponse;
    }


    /**
     * 提交问题答案
     * */
    @RequestMapping(value = "/forget_check_answer/{username}/{question}/{answer}")
    public ServerResponse forget_check_answer(@PathVariable("username") String username,@PathVariable("question") String question,
                                              @PathVariable("answer") String answer) {
        ServerResponse serverResponse=userService.forget_check_answer(username,question,answer);


        return serverResponse;
    }

    /**
     * 忘记密码的重置密码
     * */
    @RequestMapping(value = "/forget_reset_password/{username}/{passwordNew}/{forgetToken}")
    public ServerResponse forget_reset_password(@PathVariable("username") String username,
                                                @PathVariable("passwordNew") String passwordNew,
                                                @PathVariable("forgetToken") String forgetToken) {
        ServerResponse serverResponse=userService.forget_reset_password(username,passwordNew,forgetToken);


        return serverResponse;
    }



    /**
     * 检查用户名或者邮箱是否有效
     * */

    @RequestMapping(value = "/check_valid/{str}/{type}")
    public ServerResponse check_valid(@PathVariable("str") String str,
                                      @PathVariable("type") String type){
        ServerResponse serverResponse=userService.check_valid(str,type);

        return serverResponse;
    }

    /**
     * 获取登录用户信息
     * */
    @RequestMapping(value = "/get_user_info.do")
    public ServerResponse get_user_info(HttpSession session){
        UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.serverResponseByError("用户未登录");
        }
        UserInfo newUserInfo=new UserInfo();
        newUserInfo.setId(userInfo.getId());
        newUserInfo.setUsername(userInfo.getUsername());
        newUserInfo.setEmail(userInfo.getEmail());
        newUserInfo.setQuestion(userInfo.getQuestion());
        newUserInfo.setAnswer(userInfo.getAnswer());
        return ServerResponse.serverResponseBySuccess(newUserInfo);

    }

    /**
     * 登录状态重置密码
     **/
    @RequestMapping(value = "/reset_password/{passwordOld}/{passwordNew}")
    public ServerResponse reset_password(HttpSession session,
                                         @PathVariable("passwordOld") String passwordOld,
                                         @PathVariable("passwordNew") String passwordNew){
        UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.serverResponseByError("用户未登录");
        }
        return userService.reset_password(userInfo.getUsername(),passwordOld,passwordNew);


    }



    /**
     * 登录状态更新个人信息
     * */
    @RequestMapping(value = "/update_information.do")
    public ServerResponse update_information(HttpSession session,UserInfo user){
        UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.serverResponseByError("用户未登录");
        }
        user.setId(userInfo.getId());
        //更新session中的用户信息
        ServerResponse serverResponse=userService.update_information(user);
        if (serverResponse.isSuccess()){
            UserInfo userInfo1=userService.findUserInfoByUserid(userInfo.getId());
            session.setAttribute(Const.CURRENTUSER,userInfo1);
        }
        return serverResponse;
    }

    /**
     * 获取登录用户详细信息
     * */
    @RequestMapping(value = "/get_information.do")
    public ServerResponse get_information(HttpSession session){
        UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.serverResponseByError("用户未登录");
        }
        userInfo.setPassword("");
        return ServerResponse.serverResponseBySuccess(userInfo);

    }

    /**
     * 退出登录
     * */
    @RequestMapping(value = "/logout.do")
    public ServerResponse logout(HttpSession session){
        session.removeAttribute(Const.CURRENTUSER);
        return ServerResponse.serverResponseBySuccess();

    }
}
