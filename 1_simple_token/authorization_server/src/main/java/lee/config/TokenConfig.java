package lee.config;

import ch.qos.logback.core.rolling.helper.TokenConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class TokenConfig {

    //配置token的存储方法
    @Bean
    public TokenStore tokenStore() {
        //配置token存储在内存中,这种是普通token,每次都需要远程校验,性能较差
        return new InMemoryTokenStore();
    }
}