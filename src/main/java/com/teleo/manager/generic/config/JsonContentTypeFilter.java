package com.teleo.manager.generic.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import jakarta.servlet.Filter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class JsonContentTypeFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResp = (HttpServletResponse) response;
        httpResp.setContentType(MediaType.APPLICATION_JSON_VALUE);
        chain.doFilter(request, response);
    }
}
