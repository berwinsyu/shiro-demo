package indi.berwin.shirodemo;

import indi.berwin.shirodemo.util.DebugUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShiroDemoApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void testShiro(){
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject currentuser = SecurityUtils.getSubject();
        Session session = currentuser.getSession();
        session.setAttribute("key","value");
        if (!currentuser.isAuthenticated()){
            UsernamePasswordToken token = new UsernamePasswordToken("root","root");
            token.setRememberMe(true);
            try {
                currentuser.login(token);
            }
            catch (UnknownAccountException | IncorrectCredentialsException e){
                DebugUtil.wc("用户名不存在或密码错误");
            }
//            catch (IncorrectCredentialsException ice){
//                DebugUtil.wc("用户名不存在或密码错误");
//            }
            catch (Exception e){
                DebugUtil.wc("未知错误");
            }

            DebugUtil.wc(currentuser.getPrincipal() + " logged in");
            DebugUtil.wc(currentuser.getPrincipal() + " has role admin? " + currentuser.hasRole("admin"));
            DebugUtil.wc(currentuser.getPrincipal() + " has auth go?" + currentuser.isPermitted("go"));
            currentuser.logout();
            DebugUtil.wc(currentuser.getPrincipal() + "logged in");
        }
        System.exit(0);
    }

}
