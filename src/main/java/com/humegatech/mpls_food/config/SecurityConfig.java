package com.humegatech.mpls_food.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.sql.DataSource;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {
    // https://github.com/spring-projects/spring-security-samples/blob/5.8.x/servlet/spring-boot/java/hello-security-explicit/src/main/java/example/SecurityConfiguration.java

    private static final String H2_URL = "/h2-console";
    private static final String SPRING = "";
    @Autowired
    private DataSource dataSource;

    //    @Autowired
//    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder())
//                .dataSource(dataSource)
//                .usersByUsernameQuery("select username, password, enabled from users where username=?")
//                .authoritiesByUsernameQuery("select username, authority from authorities where username=?");
//    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from users where username=?")
                .authoritiesByUsernameQuery("select username, authority from authorities where username=?");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   final HandlerMappingIntrospector introspector) throws Exception {
        final MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        final MvcRequestMatcher h2 = filterPattern(mvcMatcherBuilder, "/**", H2_URL);

        http
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.ignoringRequestMatchers(h2))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(h2).permitAll()
                        .requestMatchers(filterPattern(mvcMatcherBuilder,"/css/**"),
                                filterPattern(mvcMatcherBuilder,"/js/**"),
                                filterPattern(mvcMatcherBuilder,"/images/**"),
                                filterPattern(mvcMatcherBuilder,"/days"),
                                filterPattern(mvcMatcherBuilder,"/deals"),
                                filterPattern(mvcMatcherBuilder,"/deal_logs"),
                                filterPattern(mvcMatcherBuilder,"/deal_logs/show/*"),
                                filterPattern(mvcMatcherBuilder,"/login"),
                                filterPattern(mvcMatcherBuilder,"/places"),
                                filterPattern(mvcMatcherBuilder,"/places/show"),
                                filterPattern(mvcMatcherBuilder,"/")).permitAll()
                        .requestMatchers(filterPattern(mvcMatcherBuilder,"/*/add"),
                                filterPattern(mvcMatcherBuilder,"/*/edit"),
                                filterPattern(mvcMatcherBuilder,"/uploads/**")).authenticated()
                        .requestMatchers(filterPattern(mvcMatcherBuilder,"/actuator"), // todo needed given next line?
                                filterPattern(mvcMatcherBuilder,"/actuator/*"),
                                filterPattern(mvcMatcherBuilder,"/deals/copy/"),
                                filterPattern(mvcMatcherBuilder,"/deals/delete/*"),
                                filterPattern(mvcMatcherBuilder,"/deal_logs/delete/*"),
                                filterPattern(mvcMatcherBuilder,"/places/delete/*")).hasRole("ADMIN")
                        .anyRequest().denyAll());

        return http.build();
    }

    //    @Autowired
    //    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    //        auth
    //            .inMemoryAuthentication()
    //            .withUser("user").password("password").roles("USER");
    //    }

    static MvcRequestMatcher filterPattern(final MvcRequestMatcher.Builder builder, final String pattern, final String servletPath) {
        MvcRequestMatcher matcher = builder.pattern(pattern);

        if (null !=  servletPath) {
            matcher.setServletPath(servletPath);
        }

        return matcher;
    }

    static MvcRequestMatcher filterPattern(final MvcRequestMatcher.Builder builder, final String pattern) {
        return filterPattern(builder, pattern, null);
    }

}
