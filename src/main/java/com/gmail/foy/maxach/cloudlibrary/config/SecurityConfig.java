package com.gmail.foy.maxach.cloudlibrary.config;

import com.gmail.foy.maxach.cloudlibrary.utils.JwtFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private JwtFilter jwtFilter;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()

                // only unauthorized users can auth
                .antMatchers(HttpMethod.POST, "/api/auth/*").anonymous()

                // only authorized users can get own profile
                .antMatchers(HttpMethod.GET, "/api/auth/profile").authenticated()

                // everyone can see all profiles
                .antMatchers(HttpMethod.GET, "/api/users").permitAll()

                // everyone can see others profiles
                .antMatchers(HttpMethod.GET, "/api/users/*").permitAll()

                // everyone can see users posts
                .antMatchers(HttpMethod.GET, "/api/users/*/posts").permitAll()

                // only authorized users can change user info:
                // USER can change only OWN one
                // ADMIN can change ANY ones
                .antMatchers(HttpMethod.PUT, "/api/users/*").authenticated()

                // only ADMINS can create others profiles and new admins
                .antMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")

                // only authorized users can delete only OWN profile
                .antMatchers(HttpMethod.DELETE, "/api/users/*").authenticated()

                // only authorized users can create post
                .antMatchers(HttpMethod.POST, "/api/posts").authenticated()

                // anyone can see all posts
                .antMatchers(HttpMethod.GET, "/api/posts/*").permitAll()

                // only authorized users can change posts:
                // USER can change only OWN one
                // ADMIN can change ANY ones
                .antMatchers(HttpMethod.PUT, "/api/posts/*").authenticated()

                // only authorized users can delete posts info:
                // USER can delete only OWN one
                // ADMIN can delete ANY ones
                .antMatchers(HttpMethod.DELETE, "/api/posts/*").authenticated()

                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
