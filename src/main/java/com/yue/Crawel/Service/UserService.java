package com.yue.Crawel.Service;

import com.yue.Crawel.dao.UserMapper;
import com.yue.Crawel.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Andrew on 16/5/12.
 */
@Service
public class UserService {

    @Resource
    UserMapper userMapper;

    public User login(String userName, String password, String role) {
        User user = null;
        try{
            user = userMapper.login(userName, password, role);
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public void register(String userName, String password, String role) {
        try {
            userMapper.register(userName, password, role);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<User> getAllUser() {
        List<User> allUser = null;

        try{
            allUser = userMapper.getAllUser();
        }catch (Exception e){
            e.printStackTrace();
        }
        return allUser;
    }

    public void deleteUser(String userName) throws Exception{
        userMapper.deleteUser(userName);
    }

    public void updateUser(String userName, String password, String initialUserName) throws Exception{
        userMapper.updateUser(userName, password, initialUserName);
    }

    public void createUser(String userName, String password) throws Exception{
        userMapper.createUser(userName, password);
    }
}
