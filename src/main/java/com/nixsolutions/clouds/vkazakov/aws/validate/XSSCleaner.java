package com.nixsolutions.clouds.vkazakov.aws.validate;

import com.nixsolutions.clouds.vkazakov.aws.dto.UserDto;
import org.owasp.encoder.Encode;

public class XSSCleaner {
    public static UserDto cleanFields(UserDto userDto){
        return new UserDto(
           cleanValue(userDto.getFirstName()),
           cleanValue(userDto.getLastName()),
           cleanValue(userDto.getUsername()),
           cleanValue(userDto.getEmail()),
           cleanValue(userDto.getPassword()),
           cleanValue(userDto.getBirthdate()),
           cleanValue(userDto.getRoleId()),
           cleanValue(userDto.getPhoneNumber()),
            userDto.getPhoto()
        );
    }

    public static String cleanValue(String value) {
        String result = "";
        if (value != null) {
            result = Encode.forHtmlAttribute(value);
        }
        return result;
    }
}
