package com.oyr.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oyr.properties.LoginResponseType;
import com.oyr.utils.ResultVoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义的AccessDeniedHandler实现类
 * Create by 欧阳荣
 * 2018/12/17 0:40
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        logger.error("=====403=====");
        if (LoginResponseType.JSON.equals("JSON")) { // 根据参数定义返回json 或 跳转页面
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(ResultVoUtils.error("403", e.getMessage())));
        } else {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.sendRedirect("/403");
        }
    }

}
