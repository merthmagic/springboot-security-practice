package org.xemi.poc.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "shiro")
@Component
public class ShiroMap {

    private Map<String,String> securityMap;
}
