package com.nixsolutions.clouds.vkazakov.aws.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageSns {

    private String subject;

    private String text;

}

