package com.field2fork.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomJWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	
    	String uri = request.getRequestURI();
        if (uri.contains("/auth/reset-password/")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer")) {
            String jwt = authHeader.substring(7);
            Authentication authentication = jwtUtils.populateAuthenticationTokenFromJWT(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Saved auth details under Spring Security context!");
        }
        
        

        filterChain.doFilter(request, response);
    }
    
    
}
