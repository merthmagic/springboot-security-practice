package org.xemi.poc.security.security;

import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class OwnerOfFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        Object s = req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        return false;
    }
}
