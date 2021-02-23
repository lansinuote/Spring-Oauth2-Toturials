package lee.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration

//开启oauth2,reousrce server模式
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources
                //设置我这个resource的id, 这个在auth中配置, 这里必须照抄
                .resourceId("resource1")
                .tokenStore(tokenStore)

                //这个貌似是配置要不要把token信息记录在session中
                .stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()

                //由于在zuul已经做了scope的校验,这里可以不写了.当然你想写上也是没有问题的
                .antMatchers("/**").permitAll()//.access("#oauth2.hasScope('scope1')")

                .and()

                //这个貌似是配置要不要把token信息记录在session中
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
