package com.oyr.filter;

import com.oyr.controller.ValidateCodeController;
import com.oyr.exception.ValidateCodeException;
import com.oyr.validate.code.ImageCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2018/12/25.
 */
public class ValidateCodeFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 拦截特定的uri，其他uri直接放行
        if (request.getRequestURI().equals("/authentication/login")
                && StringUtils.equalsIgnoreCase(request.getMethod(), "post")) {
            try {
                // 校验验证码是否正确
                validate(new ServletWebRequest(request));
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }

        }
        // 放行
        filterChain.doFilter(request, response);
    }

    public void validate(ServletWebRequest request) {
        String codeInRequest; // 前台传来的验证码
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "code");
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }

        ImageCode imageCode = (ImageCode) request.getRequest().getSession().getAttribute(ValidateCodeController.VALIDATE_CODE_KEY);

        if (imageCode == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (imageCode.isExpried()) {
            request.getRequest().getSession().removeAttribute(ValidateCodeController.VALIDATE_CODE_KEY);
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }

        request.getRequest().getSession().removeAttribute();
    }

}
