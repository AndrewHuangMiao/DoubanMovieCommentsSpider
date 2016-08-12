package com.yue.Crawel.Controller;

import com.yue.Crawel.Service.UserService;
import com.yue.Crawel.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrew on 16/5/12.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/login")
    public String main(){
        return "login";
    }

    @RequestMapping("/goRegister")
    public String goRegister(){
        return "register";
    }

    @RequestMapping("/checkLogin")
    public String login(HttpServletRequest request){
        Map<String,String[]> requestMap = request.getParameterMap();
        String userName = null;
        String password = null;
        String role = null;
        try{
            userName = requestMap.get("userName")[0];
            password = requestMap.get("password")[0];
            role = requestMap.get("role")[0];
            User user = userService.login(userName, password, role);
            request.getSession().setAttribute("user", user);
            if(user == null){
                return "error";
            }

            if(user.getRole().equals("user")){
                return "self_user";
            }else{
                return "self_admin";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping("/register")
    public String register(HttpServletRequest request){
        Map<String,String[]> requestMap = request.getParameterMap();
        String userName = null;
        String password = null;
        String role = null;
        try {
            userName = requestMap.get("userName")[0];
            password = requestMap.get("password")[0];
            role = requestMap.get("role")[0];
            userService.register(userName, password, role);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "login";
    }

    @RequestMapping("/admin")
    public String test(){

        return "self_admin";
    }

    @RequestMapping("/user")
    public String test2(){
        return "self_user";
    }

    @RequestMapping("/checkUser")
    @ResponseBody
    public Boolean checkUser(HttpServletRequest request){
        Map<String,String[]> requestMap = request.getParameterMap();
        String userName = null;
        String password = null;
        String role = null;
        try {
            userName = requestMap.get("userName")[0];
            password = requestMap.get("password")[0];
            role = requestMap.get("role")[0];
            User user = userService.login(userName, password, role);
            if(user!=null){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @RequestMapping("/getAllUser")
    @ResponseBody
    public List<User> getAllUser(){
        return userService.getAllUser();
    }

    @RequestMapping("/manageUser")
    public String manageUser(){
        return "manageUser";
    }

    @RequestMapping("/deleteUser")
    @ResponseBody
    public Boolean deleteUser(HttpServletRequest request){
        Map<String,String[]> requestMap = request.getParameterMap();
        String userName = null;
        try {
            userName = requestMap.get("userName")[0];
            userService.deleteUser(userName);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/updateUser")
    @ResponseBody
    public Boolean updateUser(HttpServletRequest request){
        Map<String,String[]> requestMap = request.getParameterMap();
        String userName = null;
        String password = null;
        String initialUserName = null;
        try {
            userName = requestMap.get("userName")[0];
            initialUserName = requestMap.get("initialUserName")[0];
            password = requestMap.get("password")[0];
            userService.updateUser(userName, password, initialUserName);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/createUser")
    @ResponseBody
    public Boolean createUser(HttpServletRequest request){
        Map<String,String[]> requestMap = request.getParameterMap();
        String userName = null;
        String password = null;
        try {
            userName = requestMap.get("userName")[0];
            password = requestMap.get("password")[0];
            userService.createUser(userName, password);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}

