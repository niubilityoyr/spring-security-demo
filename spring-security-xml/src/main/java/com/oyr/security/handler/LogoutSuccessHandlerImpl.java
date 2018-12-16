package com.oyr.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义的LogoutSuccessHandler实现类
 * Create by 欧阳荣
 * 2018/12/16 20:36
 */
@Service
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("登出成功！！！");
        response.sendRedirect("/login.html");
    }

}
