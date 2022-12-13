package com.example.demo.filter;

import com.example.demo.consts.AuthConsts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProdFirebaseTokenFilter extends FirebaseTokenFilter {
    public ProdFirebaseTokenFilter(UserDetailsService userDetailsService, FirebaseAuth firebaseAuth) {
        super(userDetailsService, firebaseAuth);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String token = findCookie(cookies, AuthConsts.accessTokenKey);

        try {
            String email = firebaseAuth.verifyIdToken(token).getEmail();
            UserDetails user = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception e) {
            setUnauthorizedResponse(response, "INVALID_TOKEN");
            return;
        }

        filterChain.doFilter(request, response);
    }

}
