package com.baizhi.realm;


import com.baizhi.dao.AdminDao;
import com.baizhi.entity.Admin;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class MyRealm extends AuthorizingRealm {
    @Autowired
    private AdminDao adminDao;


    //授权--对角色rol授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }


    //身份认证---对subject
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.获取令牌   身份认证是使用令牌认证，令牌存的是用户名和密码
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //2.获取token中的用户名
        Admin admin = new Admin().setUsername(token.getUsername());
        //3.根据用户名从数据库中查询一个对象信息
        Admin adminDB = adminDao.selectOne(admin);
        if (adminDB == null) {
            return null;
        } else {
            //如果根据身份信息查询到对象，说明用户名正确,验证密码
            //构造方法的参数：1.用户名，2.密码，3.盐（指定参数格式），4.当前类的名字
            SimpleAccount simpleAccount =
                    new SimpleAccount(admin.getUsername(), admin.getPassword(),
                            ByteSource.Util.bytes(adminDB.getSalt()), this.getName());
            return simpleAccount;
        }
    }

}
