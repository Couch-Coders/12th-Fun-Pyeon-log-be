package com.example.demo.filter;

import com.example.demo.consts.AuthConsts;
import com.example.demo.dto.FirebaseTokenDTO;
import com.example.demo.entity.User;
import com.example.demo.service.auth.AuthService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class FirebaseTokenFilter extends OncePerRequestFilter {

    AuthService authService;
    UserDetailsService userDetailsService;

    public FirebaseTokenFilter(AuthService authService, UserDetailsService userDetailsService) {
        this.authService = authService;
        this.userDetailsService = userDetailsService;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String token = findCookie(cookies, AuthConsts.accessTokenKey);

        try {
            FirebaseTokenDTO tokenDTO = authService.verifyIdToken(token);
            String email = authService.verifyIdToken(token).getEmail();
            User user = (User) userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            user.setUid(tokenDTO.getUid());
        } catch (Exception e) {
            setUnauthorizedResponse(response, "INVALID_TOKEN");
            return;
        }

        filterChain.doFilter(request, response);
    }

    protected String findCookie(Cookie[] cookies, String cookieName) {
        for (Cookie c : cookies) {
            if (c.getName().equals(cookieName)) {
                log.info(cookieName + " : " + c.getValue());
                return c.getValue();
            }
        }
        return null;
    }

    protected void setUnauthorizedResponse(HttpServletResponse response, String code) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"code\" : \""+code+"\"}");
    }

}
