package com.nixsolutions.clouds.vkazakov.aws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserForListDto {
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private int age;
    private String roleName;
}


