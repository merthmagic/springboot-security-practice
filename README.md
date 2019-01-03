# Security Practice

对SpringBoot+Shiro+JWT集成的一个POC

## 主要工作

- 自定义`Realm`来获取用户信息，权限信息
- 自定义拦截器
- `Shiro`在`SpringBoot`中的配置
- 集成`jBcrypt`

## 参考链接
RBAC理论部分
https://stormpath.com/blog/new-rbac-resource-based-access-control

Shiro JPA Realm 参考代码
https://github.com/antoniomaria/shiro-jpa

集成参考
https://blog.csdn.net/weixin_38132621/article/details/80216075

https://github.com/HowieYuan/Shiro-SpringBoot

JPA Hibernate版本引起的问题(发现建了一张sequence表，并且mysql自增字段配置失效)
https://stackoverflow.com/questions/41461283/hibernate-sequence-table-is-generated

一些踩坑
https://segmentfault.com/a/1190000013630601

集成jBcrypt
https://blog.csdn.net/dora_310/article/details/81107107