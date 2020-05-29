package com.example.musercurity.dsl;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Log4j2
public class MultipleAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    public MultipleAuthenticationFilter(AuthenticationManager authenticationManager, String loginUrl) {

        this.setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl(loginUrl);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {
        response.setStatus(HttpServletResponse.SC_OK);
        logger.info("Success");
    }

}
