package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
// Как я понял, эта аннотация указывается везде,
// где мы хотим конфигурировать ИМЕННО секьюрность нашего приложения!
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    public AppSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("index", "/", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(AppUserRole.USER.name())
                .anyRequest()
                .authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/management/api/v1/studens", true)
                        .and()
                    .rememberMe();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails u = User.builder()
                .username("u")
                .password(passwordEncoder.encode("u"))
                //Спринг обозначит эту роль как ROLE_USER
//                .roles(AppUserRole.USER.name())
                .authorities(AppUserRole.USER.getGrantedAuthorities())
                .build();

        UserDetails traineeAdmin = User.builder()
                .username("tr")
                .password(passwordEncoder.encode("tr"))
//                .roles(AppUserRole.TRAINEE_ADMIN.name())
                .authorities(AppUserRole.TRAINEE_ADMIN.getGrantedAuthorities())
                .build();

        UserDetails a = User.builder()
                .username("a")
                .password(passwordEncoder.encode("a"))
//                .roles(AppUserRole.ADMIN.name())
                .authorities(AppUserRole.ADMIN.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(u, a, traineeAdmin);
    }
}
