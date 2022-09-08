package com.humegatech.mpls_food.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Optional;

// modified from example at https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#auditing.auditor-aware
@Configuration
class SpringSecurityAuditorAware implements AuditorAware<String> {
    // maybe change to users.id at some point, or lock down users.username changes
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Principal::getName);
    }
}
