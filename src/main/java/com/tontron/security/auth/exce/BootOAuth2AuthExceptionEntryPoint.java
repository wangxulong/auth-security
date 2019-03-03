package com.tontron.security.auth.exce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tontron.security.auth.dto.Result;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class BootOAuth2AuthExceptionEntryPoint extends OAuth2AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws ServletException {
        Map<String, Object> map = new HashMap<String, Object>();
        Throwable cause = authException.getCause();
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            if(cause instanceof InvalidTokenException) {
                response.getWriter().write(
                        objectMapper.writeValueAsString(Result.builder().code(HttpStatus.BAD_REQUEST.value()).msg("123").build())
                );
            }else{
                response.getWriter().write(
                        objectMapper.writeValueAsString(Result.builder().code(HttpStatus.NOT_ACCEPTABLE.value()).msg("123").build())
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
