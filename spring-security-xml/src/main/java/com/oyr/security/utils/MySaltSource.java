package com.oyr.security.utils;

import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by Administrator on 2018/12/18.
 */
public class MySaltSource implements SaltSource {

    public Object getSalt(UserDetails user) {
        return null;
    }

}
