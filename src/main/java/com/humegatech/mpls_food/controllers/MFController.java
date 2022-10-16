package com.humegatech.mpls_food.controllers;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public class MFController {
    /**
     * Extract user from request.
     *
     * @param request HttpServletRequest
     * @return user
     */
    protected UsernamePasswordAuthenticationToken getUser(final HttpServletRequest request) {
        UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) request.getUserPrincipal();
        if (null != user) {
            System.out.println(String.format("USER: %s\nAUTHORITY: %s", user.getName(),
                    user.getAuthorities().stream().findFirst().get().getAuthority()));
        }

        return user;
    }
}
