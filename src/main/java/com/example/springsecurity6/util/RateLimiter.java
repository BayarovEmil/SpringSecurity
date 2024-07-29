package com.example.springsecurity6.util;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class RateLimiter implements Filter {
    private final Map<String, Integer> ipAccessMap = new HashMap<>();
    private static final int MAX_REQUESTS_PER_MINUTE = 60;
    private static final long ONE_MINUTE = 60000L;
    private long lastResetTime = System.currentTimeMillis();

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String clientIp = httpRequest.getRemoteAddr();

        synchronized (ipAccessMap) {
            if (System.currentTimeMillis() - lastResetTime > ONE_MINUTE) {
                ipAccessMap.clear();
                lastResetTime = System.currentTimeMillis();
            }
            ipAccessMap.put(clientIp, ipAccessMap.getOrDefault(clientIp, 0) + 1);
        }

        if (ipAccessMap.get(clientIp) > MAX_REQUESTS_PER_MINUTE) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
