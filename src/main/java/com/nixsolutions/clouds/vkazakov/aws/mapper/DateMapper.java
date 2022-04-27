package com.nixsolutions.clouds.vkazakov.aws.mapper;

import java.sql.Date;
import org.springframework.stereotype.Component;

@Component
public class DateMapper {

    public String map(Date date) {
    if(date == null){return "";}
        return   date.toLocalDate().toString();
    }

    public Date map(String value) {
        if(value == null || value.equals("")){return null;}
        return  Date.valueOf(value);
    }

}
