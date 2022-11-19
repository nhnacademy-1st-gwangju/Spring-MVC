package com.nhnacademy.nhnmart.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class InquiryRegisterRequest {

    @Length(min = 2, max = 200)
    private String title;

    @NotBlank
    private String category;

    @Length(max = 40000)
    private String content;

    @NotBlank
    private String author;

    private List<MultipartFile> imageFiles;
}
