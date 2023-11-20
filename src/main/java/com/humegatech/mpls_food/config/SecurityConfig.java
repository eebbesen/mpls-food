package com.humegatech.mpls_food.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {
    // https://docs.spring.io/spring-security/reference/servlet/authorization/index.html

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from users where username=?")
                .authoritiesByUsernameQuery("select username, authority from authorities where username=?");
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(
                        "/",
                        "/css/**",
                        "/places",
                        "/days",
                        "/deals",
                        "/deal_logs",
                        "/deal_logs/show/*",
                        "/images/**",
                        "/js/**",
                        "/login",
                        "/places/show/*").permitAll()
                .requestMatchers(
                        "/deals/add",
                        "/deals/edit/*",
                        "/deal_logs/add",
                        "/deal_logs/edit/*",
                        "/places/add",
                        "/uploads/**")
                .authenticated()
                .requestMatchers(
                        "/*/edit/*",
                        "/actuator",
                        "/actuator/*",
                        "/deals/delete/*",
                        "/deals/copy/*",
                        "/days/delete/*",
                        "/deal_logs/delete/*",
                        "/places/delete/*"
                        ).hasRole("ADMIN")
                .anyRequest().denyAll());
        return http.build();
    }
}
