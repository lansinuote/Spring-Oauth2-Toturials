package lee.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;

@Configuration

//开启oauth2,auth server模式
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    //首先要建表,官方给了个sql文件:https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/resources/schema.sql
    //这个sql文件不能在mysql中直接执行,要把sql中的256修改为128,LONGVARBINARY修改为BLOB
    //下面是我修改过的sql

    /*
    -- used in tests that use HSQL
    create table oauth_client_details (
      client_id VARCHAR(128) PRIMARY KEY,
      resource_ids VARCHAR(128),
      client_secret VARCHAR(128),
      scope VARCHAR(128),
      authorized_grant_types VARCHAR(128),
      web_server_redirect_uri VARCHAR(128),
      authorities VARCHAR(128),
      access_token_validity INTEGER,
      refresh_token_validity INTEGER,
      additional_information VARCHAR(4096),
      autoapprove VARCHAR(128)
    );

    create table oauth_client_token (
      token_id VARCHAR(128),
      token BLOB,
      authentication_id VARCHAR(128) PRIMARY KEY,
      user_name VARCHAR(128),
      client_id VARCHAR(128)
    );

    create table oauth_access_token (
      token_id VARCHAR(128),
      token BLOB,
      authentication_id VARCHAR(128) PRIMARY KEY,
      user_name VARCHAR(128),
      client_id VARCHAR(128),
      authentication BLOB,
      refresh_token VARCHAR(128)
    );

    create table oauth_refresh_token (
      token_id VARCHAR(128),
      token BLOB,
      authentication BLOB
    );

    create table oauth_code (
      code VARCHAR(128), authentication BLOB
    );

    create table oauth_approvals (
        userId VARCHAR(128),
        clientId VARCHAR(128),
        scope VARCHAR(128),
        status VARCHAR(10),
        expiresAt TIMESTAMP,
        lastModifiedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );


    -- customized oauth_client_details table
    create table ClientDetails (
      appId VARCHAR(128) PRIMARY KEY,
      resourceIds VARCHAR(128),
      appSecret VARCHAR(128),
      scope VARCHAR(128),
      grantTypes VARCHAR(128),
      redirectUrl VARCHAR(128),
      authorities VARCHAR(128),
      access_token_validity INTEGER,
      refresh_token_validity INTEGER,
      additionalInformation VARCHAR(4096),
      autoApproveScopes VARCHAR(128)
    );*/

    //造点测试数据
    /*
    INSERT INTO `oauth_client_details` VALUES ('client1', 'resource1', '$2a$10$YEpRG0cFXz5yfC/lKoCHJ.83r/K3vaXLas5zCeLc.EJsQ/gL5Jvum', 'scope1,scope2', 'authorization_code,password,client_credentials,implicit,refresh_token', 'http://www.baidu.com', null, '300', '1500', null, 'false');*/

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    //配置客户端
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //配置客户端存储到db
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setPasswordEncoder(passwordEncoder);
        clients.withClientDetails(clientDetailsService);
    }

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private TokenStore tokenStore;

    //配置token管理服务
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setClientDetailsService(clientDetailsService);
        defaultTokenServices.setSupportRefreshToken(true);

        //配置token的存储方法
        defaultTokenServices.setTokenStore(tokenStore);
        defaultTokenServices.setAccessTokenValiditySeconds(300);
        defaultTokenServices.setRefreshTokenValiditySeconds(1500);
        return defaultTokenServices;
    }

    //密码模式才需要配置,认证管理器
    @Autowired
    private AuthenticationManager authenticationManager;

    //把上面的各个组件组合在一起
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)//认证管理器

                //配置授权码存储到db
                .authorizationCodeServices(new JdbcAuthorizationCodeServices(dataSource))//授权码管理
                .tokenServices(tokenServices())//token管理
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    //配置哪些接口可以被访问
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")///oauth/token_key公开
                .checkTokenAccess("permitAll()")///oauth/check_token公开
                .allowFormAuthenticationForClients();//允许表单认证
    }
}
