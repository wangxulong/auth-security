package com.tontron.security.auth.oauth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class MyAuthService {

    public boolean hasPermission(HttpServletRequest request, Authentication authentication){
        String target = request.getRequestURI();
        String method = request.getMethod();
        if(target.equals("/admin/me")){
            return false;
        }
        return true;
    }
}
