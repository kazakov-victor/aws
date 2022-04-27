package com.nixsolutions.clouds.vkazakov.aws.mapper;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;

@Component
public class DateForListMapper {

    public int map(Date date) {
    if(date == null){return 0;}
        return   (int) ChronoUnit.YEARS.between(date.toLocalDate(),
                LocalDate.now());
    }

    public Date map(String value) {
        if(value == null || value.equals("")){return null;}
        return  Date.valueOf(value);
    }

}
