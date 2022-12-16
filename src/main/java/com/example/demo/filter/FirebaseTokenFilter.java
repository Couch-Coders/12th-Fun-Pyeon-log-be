package com.example.demo.filter;

import com.google.firebase.auth.FirebaseAuth;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
public abstract class FirebaseTokenFilter extends OncePerRequestFilter {
    protected UserDetailsService userDetailsService;
    protected FirebaseAuth firebaseAuth;

    protected String findCookie(Cookie[] cookies, String cookieName) {
        log.info("findCookie started in FirebaseTokenFilter");
        if (cookies == null)
            log.info("cookies is null in FirebaseTokenFilter");
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
