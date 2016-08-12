package com.yue.Crawel.dao;

import com.yue.Crawel.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Andrew on 16/5/12.
 */
public interface UserMapper {
     User login(@Param("userName")String userName, @Param("password")String password, @Param("role")String role);
    void register(@Param("userName")String userName, @Param("password")String password, @Param("role")String role);

    List<User> getAllUser();

    void deleteUser(@Param("userName")String userName);

    void updateUser(@Param("userName")String userName, @Param("password")String password, @Param("initialUserName")String initialUserName);

    void createUser(@Param("userName")String userName, @Param("password")String password);
}
