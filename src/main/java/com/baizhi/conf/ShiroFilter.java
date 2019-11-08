package com.baizhi.conf;

import com.baizhi.realm.MyRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

//定义一个shiro的拦截器，作用：未登录的用户，不能访问相应的资源
@Configuration  //表示当前的类是一个配置类
public class ShiroFilter {

    //将ShiroFilterFactoryBean交给spring工厂创建对象

    /**
     * ShiroFilterFactoryBean对象是用来做拦截器的，未登录的用户，不能访问站内资源
     * 可以在此类中进行自定义设置
     *
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager) {
        //1.创建一个shiro的拦截器类
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //2.创建shiro拦截器类必须给所在的类的SecurityManager属性赋值
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        /*
          3.定义拦截器规则:(1)匿名拦截器(2)认证拦截器
          AnonymousFilter          匿名拦截器  简称：anon   表示不拦截
          FormAuthenticationFilter 认证拦截器  简称：authc  表示拦截
          定义语法，map.put("资源路径","anon/authc(匿名/认证拦截)")
                /**表示所有资源
         */
        HashMap<String, String> map = new HashMap<>();
        map.put("/**", "authc");
        map.put("/login", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        //告知拦截器登录页面的资源路径，shiro不拦截登录页面
        shiroFilterFactoryBean.setLoginUrl("/login.jsp");
        return shiroFilterFactoryBean;
    }

    /**
     * Shiro的核心对象安全管理器SecurityManager对象交给psirng工厂创建
     * 安全管理器会调用默认的Realm去进行身份认证和凭证认证，
     * 在此我们指定自定义的身份认证和凭证对象MyRealm
     *
     * @param myRealm
     * @return
     */
    @Bean
    public SecurityManager getSecurityManager(MyRealm myRealm) {
        //根据SecurityManager接口的实现类创建一个SecurityManager
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //给安全管理器SecutiryManager设置自定义的Realm
        //realm是用来去执行身份认证和授权的
        securityManager.setRealm(myRealm);
        return securityManager;
    }

    /**
     * 自定义的MyRealm对象交给工厂创建
     * Realm是用于身份信息和凭证信息验证的，在此要给定凭证匹配器的规则
     *
     * @param hashedCredentialsMatcher
     * @return
     */
    @Bean
    public MyRealm getMyRealm(HashedCredentialsMatcher hashedCredentialsMatcher) {
        MyRealm myRealm = new MyRealm();
        //给自定义的realm设置凭证匹配器
        myRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return myRealm;
    }


    /**
     * shiro的凭证匹配器对象交给spring工厂创建，
     * HashedCredentialsMatcher： 用于判断密码是否争取，在此设置密码的加密规则
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher getHashedCredentialsMatcher() {
        //获取凭证匹配器
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //设置凭证匹配器的加密算法
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        //设置凭证匹配器的三列次数
        hashedCredentialsMatcher.setHashIterations(1024);
        return hashedCredentialsMatcher;
    }

}
