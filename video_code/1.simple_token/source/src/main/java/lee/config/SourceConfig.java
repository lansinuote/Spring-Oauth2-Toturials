package lee.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@EnableResourceServer
public class SourceConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        RemoteTokenServices tokenServices = new RemoteTokenServices();

        tokenServices.setCheckTokenEndpointUrl("http://127.0.0.1:3001/oauth/check_token");

        tokenServices.setClientId("client1");
        tokenServices.setClientSecret("123123");

        resources.resourceId("resource1")
                .tokenServices(tokenServices)
                .stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/**").access("#oauth2.hasScope('scope1')")

                .and()

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
