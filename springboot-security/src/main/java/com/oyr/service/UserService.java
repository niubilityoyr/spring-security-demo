package com.oyr.service;

import com.oyr.dao.UserMapper;
import com.oyr.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/12/21.
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findByUsername(String username){
        return userMapper.findByUsername(username);
    }

}
