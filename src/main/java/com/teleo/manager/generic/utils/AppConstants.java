package com.teleo.manager.generic.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConstants {
    public static final Long SYSTEM_ACCOUNT_ID = 1L;  // ID pour désigner le système

    public static final String PRINT_PERMISSION = "PRINT_PERMISSION";
    public static final String READ_PERMISSION = "READ_PERMISSION";
    public static final String DELET_PERMISSION = "DELET_PERMISSION";
    public static final String EDIT_PERMISSION = "EDIT_PERMISSION";
    public static final String WRITE_PERMISSION = "WRITE_PERMISSION";
    public static final String ACTIVE_ACCOUNT_PERMISSION = "ACTIVE_ACCOUNT_PERMISSION";
    public static final String CHANGE_PERMISSION = "CHANGE_PERMISSION";

    public static final String SYSTEM = "system";
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "30";

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER = "Bearer";
    public static final String HEADER_PREFIX = "Bearer ";
    public static final String TOKEN_INVALID = "INVALID";
    public static final String TOKEN_EXPIRED = "EXPIRED";
    public static final String TOKEN_VALID = "VALID";
    public static final int TOKEN_EXPIRATION = 60 * 24;

    public static final String SUPPORT_EMAIL = "vnlangessama@gmail.com";
    public static final String COMPANY_NAME = "Service Manager app";

    public static final String PERIODE_FILTABLE_FIELD = "createdAt";

    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
}
