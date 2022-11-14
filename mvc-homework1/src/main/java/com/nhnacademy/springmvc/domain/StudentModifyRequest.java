package com.nhnacademy.springmvc.domain;

import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Value
public class StudentModifyRequest {
    @NotBlank
    String name;

    @Email
    @NotBlank
    String email;

    @Min(0)
    @Max(100)
    int score;

    @NotBlank
    @Length(max = 200)
    String comment;
}
