package com.nixsolutions.clouds.vkazakov.aws.util;

public class Constants {
    public final static String EMAIL_REGEX =
            "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:" +
                    "[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    public final static String DATE_REGEX =
            "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";

    public static final String[] NO_PASSWORD_PATHS = {
        "/swagger-resources/**",
        "/v2/api-docs",
        "/swagger-ui/**",
        "/auth/sign-up",
        "/auth/sign-in",
        "/"
    };
}
