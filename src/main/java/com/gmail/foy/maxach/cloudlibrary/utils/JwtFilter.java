package com.gmail.foy.maxach.cloudlibrary.utils;

import com.gmail.foy.maxach.cloudlibrary.services.TokenService;
import com.gmail.foy.maxach.cloudlibrary.services.UserDetailsServiceImp;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Slf4j
@AllArgsConstructor
public class JwtFilter extends GenericFilterBean {


    private TokenService tokenService;
    private UserDetailsServiceImp userDetailsServiceImp;


    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ") && bearer.length() > 7) {
            return bearer.substring(7);
        }
        return null;
    }


    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        String token = getTokenFromRequest((HttpServletRequest) servletRequest);
        if (token != null) {
            Long id = tokenService.validateToken(token);

            log.info("User id from token: '{}'", id);
            UserDetailsImp userDetailsImp = userDetailsServiceImp.loadUserByUsername(Long.toString(id));
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetailsImp, null, userDetailsImp.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
