package lee.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
public class TokenConfig {

    @Autowired
    private DataSource dataSource;

    //配置token的存储方法
    @Bean
    public TokenStore tokenStore() {
        //配置token存储在内存中,这种是普通token,每次都需要远程校验,性能较差
        return new JdbcTokenStore(dataSource);
    }
}