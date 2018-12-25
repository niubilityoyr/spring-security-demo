package com.oyr.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oyr.properties.LoginResponseType;
import com.oyr.utils.ResultVoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
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
public class AuthenticationFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        logger.error("=====认证失败=====");
        if (LoginResponseType.JSON.equals("JSON")) { // 根据参数定义返回json或跳转页面
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(ResultVoUtils.error("500", e.getMessage())));
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.sendRedirect("/loginError");
        }
    }

}
