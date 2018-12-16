package com.oyr.security.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义的AccessDeniedHandler实现类
 * Create by 欧阳荣
 * 2018/12/17 0:40
 */
@Service
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        System.out.println("403 访问被拒绝");
        response.sendRedirect("/403.html");
    }

}
