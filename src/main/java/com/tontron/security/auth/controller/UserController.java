package com.tontron.security.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class UserController {
    @Autowired
    private RedisTokenStore redisTokenStore;

    @GetMapping("/user/me")
    public String index(){
        return "user me";
    }

    @GetMapping("/admin/me")
    public String userInfo(){
        return "index.html";
    }


    @GetMapping("/oauth/remove")
    public String remove() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Collection<OAuth2AccessToken> result = redisTokenStore.findTokensByClientIdAndUserName("wxl","wxl");
       for(OAuth2AccessToken auth2AccessToken:result){
           redisTokenStore.removeAccessToken(auth2AccessToken);
       }

        return objectMapper.writeValueAsString(result);
    }
    @GetMapping("/oauth/getToken")
    public String getToken() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Collection<OAuth2AccessToken> result = redisTokenStore.findTokensByClientIdAndUserName("wxl","wxl");

        return objectMapper.writeValueAsString(result);
    }
}
