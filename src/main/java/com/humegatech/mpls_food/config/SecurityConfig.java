package com.humegatech.mpls_food.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // https://docs.spring.io/spring-security/site/docs/5.3.9.RELEASE/reference/html5/#servlet-authorization

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from users where username=?")
                .authoritiesByUsernameQuery("select username, authority from authorities where username=?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/css/**", "/places", "/deals", "/days", "/deal_logs", "/login", "/", "/js/**",
                        "/images/**", "/places/show/*", "/deal_logs/show/*").permitAll()
                .mvcMatchers("/deals/add", "/places/add", "/uploads/**", "/deals/edit/*", "/deal_logs/add",
                        "/deal_logs/edit/*")
                .authenticated()
                .mvcMatchers("/places/delete/*", "/*/edit/*", "/deals/delete/*", "/deals/copy/*",
                        "/days/delete/*", "/deal_logs/delete/*", "/actuator", "/actuator/*", "").hasRole("ADMIN")

                .anyRequest().denyAll();
        // http.httpBasic();
        http.formLogin();
    }

    //    @Autowired
    //    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    //        auth
    //            .inMemoryAuthentication()
    //            .withUser("user").password("password").roles("USER");
    //    }

}
