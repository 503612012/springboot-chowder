package com.oven.shiro;

import com.oven.entity.User;
import com.oven.service.RoleMenuService;
import com.oven.service.RoleService;
import com.oven.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;

public class MyShiroRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private RoleMenuService roleMenuService;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User) principals.getPrimaryPrincipal();
        List<String> rolesSet = roleService.findByUserId(user.getId());
        List<String> permsSet = roleMenuService.findByUserId(user.getId());

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(new HashSet<>(rolesSet));
        info.setStringPermissions(new HashSet<>(permsSet));
        return info;
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userName = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        User user = userService.findByUserName(userName);
        if (user == null) {
            throw new UnknownAccountException("账户不存在！");
        }
        if (!password.equals(user.getPassword())) {
            throw new IncorrectCredentialsException("密码错误！");
        }
        return new SimpleAuthenticationInfo(user, password, getName());
    }

}