package com.hospital.management.config;

import java.io.IOException;
import java.util.Collections;

//import jakarta.servlet.http.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

 @Autowired
 private JwtUtil jwtUtil;

 @Override
 protected void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain filterChain) throws ServletException, IOException {

     String authHeader = request.getHeader("Authorization");
     if (authHeader != null && authHeader.startsWith("Bearer ")) {
         String token = authHeader.substring(7);
         try {
             String email = jwtUtil.extractEmail(token);
             if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                 UsernamePasswordAuthenticationToken authentication =
                     new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
                 SecurityContextHolder.getContext().setAuthentication(authentication);
             }
         } catch (JwtException e) {
             // Clear context and optionally log the exception for debugging
             SecurityContextHolder.clearContext();
         }
     }
     filterChain.doFilter(request, response);
 }
}
