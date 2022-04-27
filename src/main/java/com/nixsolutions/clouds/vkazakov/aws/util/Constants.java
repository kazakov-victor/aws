package com.nixsolutions.clouds.vkazakov.aws.util;

public class Constants {
    public final static String ID_REGEX = "^[0-9]+$";

    public final static String EMAIL_REGEX =
            "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:" +
                    "[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    public final static String DATE_REGEX =
            "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
}
