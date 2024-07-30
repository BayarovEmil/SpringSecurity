package com.example.springsecurity6.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Enumeration;

public class IpUtil {
    public static String getClientIp(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            System.out.println(headerName + ": " + headerValue);
        }

        String header = request.getHeader("X-Forwarded-For");
        if (header == null || header.isEmpty() || "unknown".equalsIgnoreCase(header)) {
            header = request.getHeader("Proxy-Client-IP");
        }
        if (header == null || header.isEmpty() || "unknown".equalsIgnoreCase(header)) {
            header = request.getHeader("WL-Proxy-Client-IP");
        }
        if (header == null || header.isEmpty() || "unknown".equalsIgnoreCase(header)) {
            header = request.getHeader("HTTP_CLIENT_IP");
        }
        if (header == null || header.isEmpty() || "unknown".equalsIgnoreCase(header)) {
            header = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (header == null || header.isEmpty() || "unknown".equalsIgnoreCase(header)) {
            header = request.getRemoteAddr();
        }
        // X-Forwarded-For başlığında çoxlu IP adresləri ola bilər, ilkini istifadə edək
        if (header != null && header.contains(",")) {
            header = header.split(",")[0];
        }
        return header;
    }
}
