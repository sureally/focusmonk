package com.netease.focusmonk.filter;

import com.netease.focusmonk.config.ParameterRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hejiecheng
 * @Date 2019-05-10
 */
@Slf4j
@Configuration
@WebFilter(urlPatterns = "/*")
public class JwtDecodeFilter implements Filter {

    private final static String[] EXCLUDE = {
            "/SDSController/login",
            "/SDSController/sendSMS"
    };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getRequestURI();
        if (!Arrays.asList(EXCLUDE).contains(uri)) {
            log.info("decode:{}", uri);
            String jwt = request.getParameter("jwt");
            Map map = new HashMap(request.getParameterMap());
            log.info("before:{}", jwt);
            map.put("jwt", URLDecoder.decode(jwt, "UTF-8"));
            log.info("after:{}", map.get("jwt"));
            ParameterRequestWrapper wrapRequest = new ParameterRequestWrapper(request, map);
            filterChain.doFilter(wrapRequest, servletResponse);
        } else {
            log.info("nodecode:{}", uri);
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
