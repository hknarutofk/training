package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * @author yeqiang
 * @since 11/18/20 2:32 PM
 */
@Configuration
@EnableWebSecurity
// @EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder().username("user")
            .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW").roles("USER").build();
        return new InMemoryUserDetailsManager(user);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(a -> a.antMatchers("/", "/**.html", "/error", "/webjars/**", "/login").permitAll()
            .anyRequest().authenticated())
            .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
            .oauth2Login().and().logout(l -> l.logoutSuccessUrl("/").permitAll())
            // csrf token 放入cookie
            .csrf(c -> c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
    }
}
