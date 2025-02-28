package com.watermelon.server.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

import java.util.Arrays;
import java.util.List;

//
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class CorsFilter implements Filter {
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) res;
//
//        //response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
//
//        List<String> allowedOrigins = Arrays.asList("http://localhost:5173", "http://localhost:63342", "https://watermelon-clap.web.app");
//        String origin = request.getHeader("Origin");
//
//        if (allowedOrigins.contains(origin)) {
//            response.setHeader("Access-Control-Allow-Origin", origin);
//        }
//
//
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Methods","GET, POST, PUT, DELETE, PATCH, OPTIONS");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers",
//                "Origin, X-Requested-With, Content-Type, Accept, Authorization, ApplyTicket");
//
//        if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//            response.setStatus(HttpServletResponse.SC_OK);
//        }else {
//            chain.doFilter(req, res);
//        }
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//}