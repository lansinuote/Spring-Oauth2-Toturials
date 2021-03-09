package lee.config;

import lee.model.MyUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .anyRequest().permitAll()

                .and()
                .formLogin()

                .and()
                .logout();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetailsService userDetailsService = new UserDetailsService() {

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                if (username.equals("user")){
                    MyUserDetails user = new MyUserDetails();
                    user.setUsername("user");
                    user.setPassword(passwordEncoder().encode("user"));
                    user.setPerms("user");
                    return user;
                }


                if (username.equals("admin")){
                    MyUserDetails admin = new MyUserDetails();
                    admin.setUsername("admin");
                    admin.setPassword(passwordEncoder().encode("admin"));
                    admin.setPerms("admin");
                    return admin;
                }

                return null;
            }
        };

        return userDetailsService;
    }
}
