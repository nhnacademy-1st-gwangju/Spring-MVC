package com.nhnacademy.springmvc.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
