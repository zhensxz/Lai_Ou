package com.example.Backend.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AuthContext {

    public static String getUsername() {
        HttpServletRequest req = currentRequest();
        return req != null ? (String) req.getAttribute("auth.username") : null;
    }

    public static String getRole() {
        HttpServletRequest req = currentRequest();
        Object r = req != null ? req.getAttribute("auth.role") : null;
        return r != null ? String.valueOf(r) : null;
    }

    public static Long getUserId() {
        HttpServletRequest req = currentRequest();
        Object id = req != null ? req.getAttribute("auth.uid") : null;
        if (id == null) return null;
        if (id instanceof Long l) return l;
        if (id instanceof Integer i) return i.longValue();
        return Long.valueOf(String.valueOf(id));
    }

    private static HttpServletRequest currentRequest() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes sra) {
            return sra.getRequest();
        }
        return null;
    }
}


