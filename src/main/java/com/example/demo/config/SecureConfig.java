package com.example.demo.config;

import com.example.demo.filter.FirebaseTokenFilter;
import com.example.demo.filter.FirebaseTokenFilterFactory;
import com.example.demo.filter.InterFirebaseTokenFilter;
import com.example.demo.filter.ProdFirebaseTokenFilter;
import com.google.firebase.auth.FirebaseAuth;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecureConfig {

    private UserDetailsService userDetailsService;

    private FirebaseAuth firebaseAuth;

    private FirebaseTokenFilterFactory firebaseTokenFilterFactory;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        setSecurityConfigs(http);

        http.addFilterBefore(firebaseTokenFilterFactory.getInstance(userDetailsService, firebaseAuth),
                UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    public void setSecurityConfigs(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .csrf().disable();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .antMatchers(HttpMethod.GET, "/users/me")
                .antMatchers("/favicon.ico")
                .antMatchers("/hello")
                .antMatchers(HttpMethod.GET, "/stores/*/reviews")
                .antMatchers(HttpMethod.GET, "/stores")
                .antMatchers(HttpMethod.GET, "/stores/*");

    }

}
