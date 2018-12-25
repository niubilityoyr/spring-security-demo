package com.oyr.service;

import com.oyr.dao.PermissionMapper;
import com.oyr.domain.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/12/21.
 */
@Service
public class PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    public List<Permission> findListByUserId(Integer userId) {
        return permissionMapper.findByUserId(userId);
    }

}
