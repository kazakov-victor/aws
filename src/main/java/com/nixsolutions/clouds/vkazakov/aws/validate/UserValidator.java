package com.nixsolutions.clouds.vkazakov.aws.validate;

import com.nixsolutions.clouds.vkazakov.aws.dto.UserDto;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class UserValidator {
    public static Map<String, String> validate(UserDto userDto) {
        Map<String, String> errors = new HashMap<>();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserDto>> constraintViolations
                = validator.validate(userDto);

        if (constraintViolations.isEmpty() && !userDto.getPassword()
                .equals(userDto.getPasswordAgain())) {
            errors.put("password", "Password again must be equals to password ");
        }

        checkAge(userDto, errors);

        if (!constraintViolations.isEmpty()) {
            for (ConstraintViolation<UserDto> constraintViolation
                    : constraintViolations) {
                errors.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
            }
        }
        return errors;
    }

    public static void checkAge(UserDto userDto, Map<String, String> errors) {
        int ages = getAges(toLocalDate(userDto.getBirthdate()));

        if (ages > 100) {
            errors.put("dateOfBirth", "You are too old!");
        }

        if (ages < 1) {
            errors.put("dateOfBirth", "Incorrect date!");
        }
    }
    public static int getAges(LocalDate dateOfBirth) {
        Period period = Period.between(dateOfBirth, LocalDate.now());
        return period.getYears();
    }

    public static LocalDate toLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
        return LocalDate.parse(date, formatter);
    }
}
