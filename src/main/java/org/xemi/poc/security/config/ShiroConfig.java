package org.xemi.poc.security.config;

import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.TextConfigurationRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.xemi.poc.security.security.CustomRealm;
import org.xemi.poc.security.security.JwtAuthcFilter;
import org.xemi.poc.security.security.OwnerOfFilter;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;


/**
 * 配置shiro
 *
 * @see <a href="https://github.com/HowieYuan/Shiro-SpringBoot/blob/master/shiroJWT/src/main/java/com/howie/shirojwt/config/ShiroConfig.java">shiro example</a>
 * @see <a href="https://segmentfault.com/a/1190000013630601">shiro踩坑</a>
 */
@Configuration
@Component
public class ShiroConfig {


    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ShiroMap shiroMap;

    /**
     * 创建 Realm
     *
     * @see <a href="https://shiro.apache.org/realm.html">Realm</a>
     */
    /*
    @Bean
    public Realm realm() {
        TextConfigurationRealm realm = new TextConfigurationRealm();
        realm.setUserDefinitions("joe.coder=password,user\n" +
                "jill.coder=password,admin");

        realm.setRoleDefinitions("admin=read,write\n" +
                "user=read");
        realm.setCachingEnabled(true);
        return realm;
    }
    */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        //将下面的注释了，过滤规则移到ShiroFilterFactoryBean
        /*
        chainDefinition.addPathDefinition("/login", "anon");

        chainDefinition.addPathDefinition("/admin/login", "anon");

        // logged in users with the 'admin' role
        chainDefinition.addPathDefinition("/admin/**", "authc, roles[admin]");

        // logged in users with the 'document:read' permission
        chainDefinition.addPathDefinition("/docs/**", "authc, perms[document:read]");

        // all other paths require a logged in user
        chainDefinition.addPathDefinition("/**", "authc");
        */
        return chainDefinition;
    }

    @Bean
    protected CacheManager cacheManager() {
        return new MemoryConstrainedCacheManager();
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();

        filters.put("jwt", new JwtAuthcFilter());
        filters.put("ownerOf",new OwnerOfFilter());

        //这里定义ShiroFilterFactoryBean后，似乎使properties文件中的shiro.loginUrl配置失效了
        //所以增加了这个配置
        shiroFilterFactoryBean.setLoginUrl("/error-401");
        shiroFilterFactoryBean.setUnauthorizedUrl("/error-403");

        // 拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边
        // authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问

        /*
        filterChainDefinitionMap.put("/register", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/admin/login", "anon");
        filterChainDefinitionMap.put("/ping", "jwt, roles[root]");
        filterChainDefinitionMap.put("/admin/**", "jwt, roles[admin]");
        filterChainDefinitionMap.put("/**", "jwt");
*/
        Map<String, String> rules = prepareMap(shiroMap.getSecurityMap());

        filterChainDefinitionMap.putAll(rules);

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    private Map<String, String> prepareMap(Map<String, String> raw) {
        Map<String, String> ret = new LinkedHashMap<>();
        for (String key : raw.keySet()) {
            String val = raw.get(key);
            key = key.replace('[', ' ');
            key = key.replace(']', ' ');
            key = key.trim();
            ret.put(key, val);
        }
        return ret;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(CustomRealm customRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置自定义 realm.
        securityManager.setRealm(customRealm);

        //
        //关闭shiro自带的session，详情见文档
        //http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
        //
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }


    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public Authorizer authorizer() {
        return applicationContext.getBean(CustomRealm.class);
    }
}
