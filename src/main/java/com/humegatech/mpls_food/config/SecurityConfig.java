package com.humegatech.mpls_food.config;

import javax.sql.DataSource;

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
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@EnableWebSecurity
@EnableMethodSecurity()
@Configuration
public class SecurityConfig {
    // https://docs.spring.io/spring-security/reference/servlet/authorization/index.html

    private final DataSource dataSource;

    @Autowired
    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from users where username=?")
                .authoritiesByUsernameQuery("select username, authority from authorities where username=?");
    }

    /**
     * Due to H2's additional servlet MvcRequestMatcher must be used.
     *
     * @param http HttpSecurity
     * @param introspector HandlerMappingIntrospector
     * @return SecurityFilterChain
     * @throws Exception formLogin Exception
     */
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                      final HandlerMappingIntrospector introspector) throws Exception {
        final MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        final MvcRequestMatcher h2 = filterPattern(mvcMatcherBuilder, "/**", "/h2-console");

        http.formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.ignoringRequestMatchers(h2))
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(
                        h2,
                        filterPattern(mvcMatcherBuilder, "/"),
                        filterPattern(mvcMatcherBuilder, "/css/**"),
                        filterPattern(mvcMatcherBuilder, "/places"),
                        filterPattern(mvcMatcherBuilder, "/days"),
                        filterPattern(mvcMatcherBuilder, "/deals"),
                        filterPattern(mvcMatcherBuilder, "/deal_logs"),
                        filterPattern(mvcMatcherBuilder, "/deal_logs/show/*"),
                        filterPattern(mvcMatcherBuilder, "/images/**"),
                        filterPattern(mvcMatcherBuilder, "/js/**"),
                        filterPattern(mvcMatcherBuilder, "/login"),
                        filterPattern(mvcMatcherBuilder, "/places/show/*")).permitAll()
                .requestMatchers(
                        filterPattern(mvcMatcherBuilder, "/deals/add"),
                        filterPattern(mvcMatcherBuilder, "/deals/edit/*"),
                        filterPattern(mvcMatcherBuilder, "/deal_logs/add"),
                        filterPattern(mvcMatcherBuilder, "/deal_logs/edit/*"),
                        filterPattern(mvcMatcherBuilder, "/places/add"),
                        filterPattern(mvcMatcherBuilder, "/uploads/**"))
                .authenticated()
                .requestMatchers(
                        filterPattern(mvcMatcherBuilder, "/*/edit/*"),
                        filterPattern(mvcMatcherBuilder, "/actuator/**"),
                        filterPattern(mvcMatcherBuilder, "/deals/delete/*"),
                        filterPattern(mvcMatcherBuilder, "/deals/copy/*"),
                        filterPattern(mvcMatcherBuilder, "/days/delete/*"),
                        filterPattern(mvcMatcherBuilder, "/deal_logs/delete/*"),
                        filterPattern(mvcMatcherBuilder, "/places/delete/*")
                        ).hasRole("ADMIN")
                .anyRequest().denyAll());
        return http.build();
    }

    /**
     * Helper to create MvcRequestMatcher.
     *
     * @param builder MvcRequestMatcher.Builder
     * @param pattern String
     * @param servletPath if null default SpringMVC path used
     * @return MvcRequestMatcher
     */
    private static MvcRequestMatcher filterPattern(final MvcRequestMatcher.Builder builder,
                                                   final String pattern,
                                                   final String servletPath) {
        MvcRequestMatcher matcher = builder.pattern(pattern);

        if (null != servletPath) {
            matcher.setServletPath(servletPath);
        }

        return matcher;
    }

    private static MvcRequestMatcher filterPattern(final MvcRequestMatcher.Builder builder, final String pattern) {
        return filterPattern(builder, pattern, null);
    }
}
