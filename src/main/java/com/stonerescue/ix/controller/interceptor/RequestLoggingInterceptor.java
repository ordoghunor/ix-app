package com.stonerescue.ix.controller.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    @Autowired
    private HttpServletRequest request;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getHeader("X-Forward-For");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        Enumeration<String> headerNames = request.getHeaderNames();

        StringBuilder allHeaders = new StringBuilder();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            allHeaders.append(headerName).append(": ").append(headerValue).append("\n");
        }

        MDC.put("userAgent", userAgent);
        MDC.put("clientIp", ipAddress);
        MDC.put("serverName", request.getServerName());
        MDC.put("remoteHost", request.getRemoteHost());
        MDC.put("remoteUser", request.getRemoteUser());
        MDC.put("headerNames", allHeaders.toString());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;

        MDC.put("responseTime", String.valueOf(responseTime));
        logger.info("Server Response Time: {} milliseconds", responseTime);

        MDC.clear();
    }
}
