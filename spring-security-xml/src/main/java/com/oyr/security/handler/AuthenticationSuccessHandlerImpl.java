package com.oyr.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义的一个 AuthenticationSuccessHandler 的实现类。
 * Create by 欧阳荣
 * 2018/12/16 18:16
 */
@Service
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("登录成功！！！！");
        response.sendRedirect("/index.html");
    }

}
