package com.nhnacademy.nhnmart.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRegisterRequest {

    @Length(min = 1, max = 40000)
    private String comment;

    @NotBlank
    private String adminName;
}
