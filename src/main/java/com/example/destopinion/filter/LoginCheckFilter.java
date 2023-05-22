package com.example.destopinion.filter;

import com.alibaba.fastjson2.JSON;
import com.example.destopinion.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
//@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //过滤器，进行相关内容的过滤
    public static final AntPathMatcher antPathMatcher=new AntPathMatcher();//这是进行匹配用

    private List<String> allowed = Arrays.asList("login", "register");
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        log.info("开启拦截");
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;
        String  url = request.getRequestURL().toString();

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Content-Type,authorization,token");
        response.setHeader("Access-Control-Expose-Headers","authorization");

        int lastIndex = url.lastIndexOf('/');
        String ur = url.substring(lastIndex+1);

        System.out.println(ur);

        if(allowed.contains(ur)){
            filterChain.doFilter(request,response);
            return;
        }

        String token = request.getHeader("token");

        if(token == null){
            //说明没有携带token
            response.getWriter().write(JSON.toJSONString("NOTLOGIN"));
            return;
        }

        boolean b = JwtUtils.checkToken(token);
        if(b){
            filterChain.doFilter(request,response);
            return;
        }
        log.info("用户信息验证失败");
        response.getWriter().write(JSON.toJSONString("ISNOTUSER"));
    }
}
