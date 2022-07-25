package com.rpdpymnt.reporting.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

class CustomServletRequestWrapper extends HttpServletRequestWrapper {

    private final String customParam;
    private static final String AUTH_USER_ID = "authuserid";

    CustomServletRequestWrapper(HttpServletRequest request, String authuserid) {
        super(request);
        this.customParam = authuserid;
    }

    @Override
    public String getHeader(String name) {
        if (AUTH_USER_ID.equals(name)) {
            return this.customParam;
        }
        String header = super.getHeader(name);
        return (header != null) ? header : super.getParameter(name);
    }

    /**
     * Extended to include auth user header so that @RequestHeader annotation works on controller methods.
     */
    @Override
    public Enumeration<String> getHeaders(String name) {
        Set<String> set = new HashSet<>();

        Enumeration<String> names = super.getHeaders(name);
        while (names.hasMoreElements()) {
            set.add(names.nextElement());
        }

        if (AUTH_USER_ID.equals(name)) {
            set.add(customParam);
        }

        return Collections.enumeration(set);
    }
}
