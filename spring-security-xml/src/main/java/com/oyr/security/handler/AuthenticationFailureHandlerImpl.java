package com.oyr.security.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义的一个 AuthenticationSuccessHandler 的实现类。
 * Create by 欧阳荣
 * 2018/12/16 18:20
 */
@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        // 认证失败
        System.out.println("认证失败");
        response.sendRedirect("/loginError.html");
    }

}
