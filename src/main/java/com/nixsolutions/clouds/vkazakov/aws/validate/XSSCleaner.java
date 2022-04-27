package com.nixsolutions.clouds.vkazakov.aws.validate;

import com.nixsolutions.clouds.vkazakov.aws.dto.UserDto;
import org.owasp.encoder.Encode;
import org.springframework.stereotype.Component;

@Component
public class XSSCleaner {

    public UserDto cleanFields(UserDto userDto){
        return new UserDto(
           cleanValue(userDto.getFirstName()),
           cleanValue(userDto.getLastName()),
           cleanValue(userDto.getUsername()),
           cleanValue(userDto.getEmail()),
           cleanValue(userDto.getPassword()),
           cleanValue(userDto.getPasswordAgain()),
           cleanValue(userDto.getBirthdate()),
           cleanValue(userDto.getRoleId()),
           cleanValue(userDto.getPhoneNumber()),
            userDto.getPhoto()
        );
    }

    public String cleanValue(String value) {
        String result = "";
        if (value != null) {
            result = Encode.forHtmlAttribute(value);
        }
        return result;
    }
}
