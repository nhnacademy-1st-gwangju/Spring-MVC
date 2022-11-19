package com.nhnacademy.nhnmart.controller;

import com.nhnacademy.nhnmart.domain.Inquiry;
import com.nhnacademy.nhnmart.domain.dto.InquiryRegisterRequest;
import com.nhnacademy.nhnmart.exception.InquiryNotFoundException;
import com.nhnacademy.nhnmart.exception.ValidationFailedException;
import com.nhnacademy.nhnmart.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/cs")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryRepository inquiryRepository;

    // GET: "/cs" 고객 메인(내가 문의한 목록)
    @GetMapping
    public String userMain(ModelMap modelMap) {
        List<Inquiry> inquiries = inquiryRepository.getAllInquiries();

        modelMap.put("inquiries", inquiries);
        return "userMain";
    }

    // GET: "/cs/inquiry" 고객 문의 폼
    @GetMapping("/inquiry")
    public String inquiryForm() {
        return "inquiryRegister";
    }

    // POST: "/cs/inquiry" 고객 문의 처리
    @PostMapping("/inquiry")
    public String doInquiry(@Valid @ModelAttribute InquiryRegisterRequest request,
                            @Value("${upload.dir}") String uploadDir,
                            BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        List<MultipartFile> imageFiles = request.getImageFiles();

        if (!imageFiles.get(0).isEmpty()) {
            for (MultipartFile file : imageFiles) {
                file.transferTo(Paths.get(uploadDir + file.getOriginalFilename()));
            }

            List<String> fileDirs = imageFiles.stream()
                    .map(MultipartFile::getOriginalFilename)
                    .collect(Collectors.toList());
            inquiryRepository.registerInquiry(request.getTitle(), request.getCategory(), request.getContent(), request.getAuthor(), fileDirs);
        } else {
            inquiryRepository.registerInquiry(request.getTitle(), request.getCategory(), request.getContent(), request.getAuthor(), new ArrayList<>());
        }
        return "redirect:/cs";
    }

    // GET: "/cs/inquiry/{inquiryId}" 문의 상세 페이지
    @GetMapping("/inquiry/{inquiryId}")
    public String inquiryDetail(@PathVariable long inquiryId, ModelMap modelMap) {
        Inquiry inquiry = inquiryRepository.getInquiry(inquiryId);

        if (Objects.isNull(inquiry)) {
            throw new InquiryNotFoundException(String.valueOf(inquiryId));
        }

        modelMap.put("inquiry", inquiry);
        return "inquiryView";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource showImage(@PathVariable("filename") String filename) throws MalformedURLException {
        String uploadDir = "/Users/hakhyeonsong/testupload/";
        return new UrlResource("file:" + uploadDir + filename);
    }
}
