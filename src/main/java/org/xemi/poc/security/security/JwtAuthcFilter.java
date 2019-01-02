package org.xemi.poc.security.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthcFilter extends BasicHttpAuthenticationFilter {

    private static final String AUTH_HEADER = "Authorization";

    /**
     * 判断登录
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) {
        if (((HttpServletRequest) servletRequest).getHeader(AUTH_HEADER) != null) {
            try {
                executeLogin(servletRequest, servletResponse);
                return true;
            } catch (Exception e) {
                responseError(servletResponse, e.getMessage());
                return false;
            }
        }
        //如果请求头不存在 Token，则可能是执行登陆操作或者是游客状态访问，无需检查 token，直接返回 true
        responseError(servletResponse, "需要登录");
        return false;
    }

    /**
     * 登录实作
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(AUTH_HEADER);
        JwtAuthenticationToken jwtToken = new JwtAuthenticationToken(token);

        if (!jwtToken.isValid())
            throw new Exception("AccessToken is invalid or expired");

        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);

        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // CORS preflight
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }

        /*
        Subject subject = SecurityUtils.getSubject();
        boolean isAuthenticated = subject.isAuthenticated();
        if (!isAuthenticated) {
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().print("NO AUTH!");
            return false;
        }
        */

        return super.preHandle(request, response);
    }

    /**
     * 输出错误信息
     */
    private void responseError(ServletResponse response, String message) {
        PrintWriter out = null;
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            HashMap<String, String> map = new HashMap<>();
            map.put("status", "failed");
            map.put("message", message);
            httpServletResponse.setStatus(401);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(map);
            httpServletResponse.setContentType("application/json; charset=utf-8");
            httpServletResponse.setCharacterEncoding("UTF-8");

            out = response.getWriter();
            out.append(json);
            httpServletResponse.flushBuffer();
        } catch (IOException ex) {
            log.error("序列化输出错误信息失败", ex);
        } finally {
            if (out != null)
                out.close();
        }
    }
}
