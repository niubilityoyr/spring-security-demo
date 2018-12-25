package com.oyr.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oyr.properties.LoginResponseType;
import com.oyr.utils.ResultVoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义的LogoutSuccessHandler实现类
 * Create by 欧阳荣
 * 2018/12/16 20:36
 */
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("=====登出成功=====");
        if (LoginResponseType.JSON.equals("JSON")) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(ResultVoUtils.ok("登出成功")));
        } else {
            response.sendRedirect("/logoutSuccess");
        }
    }

}
