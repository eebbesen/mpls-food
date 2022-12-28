//package com.humegatech.mpls_food.config;

//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.util.matcher.RegexRequestMatcher;
//
//import javax.sql.DataSource;
//
////@Configuration
////@EnableWebSecurity
////@EnableGlobalMethodSecurity(securedEnabled = true)
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private DataSource dataSource;
//
//    @Autowired
//    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder())
//                .dataSource(dataSource)
//                .usersByUsernameQuery("select username, password, enabled from users where username=?")
//                .authoritiesByUsernameQuery("select username, authority from authorities where username=?")
//        ;
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
////                .anyRequest().authenticated()
//                .and()
//                .formLogin().permitAll()
//                .and()
//                .logout().permitAll()
//                .and()
//                .requestMatcher(new RegexRequestMatcher("$.*/", "PUT")).authorizeRequests();
////                .and()
////                .requestMatcher(new RegexRequestMatcher("$.*/", "POST")).authorizeRequests();
////                .and()
////                .requestMatcher(new RegexRequestMatcher("$.*/", "PATCH")).authorizeRequests()
////                .and()
////                .requestMatcher(new RegexRequestMatcher("$.*/", "DELETE")).authorizeRequests();
//        ;
//        ;
//    }
//}