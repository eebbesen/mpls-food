package com.humegatech.mpls_food.util;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;


@Component
public class WebUtils {

    public static final String MSG_SUCCESS = "MSG_SUCCESS";
    public static final String MSG_INFO = "MSG_INFO";
//    public static final String MSG_ERROR = "MSG_ERROR";
    private static MessageSource messageSource;
    private static LocaleResolver localeResolver;

    private WebUtils(final MessageSource messageSource, final LocaleResolver localeResolver) {
        WebUtils.messageSource = messageSource;
        WebUtils.localeResolver = localeResolver;
    }

    public static HttpServletRequest getRequest() {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (null == requestAttributes) {
            return null;
        }

        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    public static String getMessage(final String code, final Object... args) {
        return messageSource.getMessage(code, args, code, localeResolver.resolveLocale(Objects.requireNonNull(getRequest())));
    }

    // used by Thymeleaf -- do not remove
    public static boolean isRequiredField(final Object dto, final String fieldName) throws
            NoSuchFieldException {
        return dto.getClass().getDeclaredField(fieldName).getAnnotation(NotNull.class) != null;
    }

}
