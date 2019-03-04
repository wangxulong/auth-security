package com.tontron.security.auth.sms;

import com.tontron.security.ssosecurity.handler.LoginErrorHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SmsCodeFilter extends OncePerRequestFilter {
    private LoginErrorHandler loginErrorHandler;

    public LoginErrorHandler getLoginErrorHandler() {
        return loginErrorHandler;
    }

    public void setLoginErrorHandler(LoginErrorHandler loginErrorHandler) {
        this.loginErrorHandler = loginErrorHandler;
    }

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if(StringUtils.endsWithIgnoreCase("/doLogin",request.getRequestURI())
         && StringUtils.endsWithIgnoreCase("POST",request.getMethod())
        ){
            try {
                validate(new ServletWebRequest(request));
            }catch (ValidateCodeException e){
                loginErrorHandler.onAuthenticationFailure(request,response,e);
                return;
            }

        }

        filterChain.doFilter(request,response);
    }

    private void validate(ServletWebRequest request) throws ServletRequestBindingException {

        ImageCode codeInSession = (ImageCode)
                sessionStrategy.getAttribute(request,ValidateCodeController.SESSION_IMAGE_KEY);
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),"code");

        if(StringUtils.isBlank(codeInRequest)){
            throw new ValidateCodeException("验证码不能为空");
        }

        if(codeInSession == null){
            throw new ValidateCodeException("验证码不存在");
        }

        if(!StringUtils.equals(codeInSession.getCode(),codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }


        sessionStrategy.removeAttribute(request,ValidateCodeController.SESSION_IMAGE_KEY);



    }
}
