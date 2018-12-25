package com.oyr.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by Administrator on 2018/12/25.
 */
public class ValidateCodeException extends AuthenticationException {

    public ValidateCodeException(String msg) {
        super(msg);
    }

}
