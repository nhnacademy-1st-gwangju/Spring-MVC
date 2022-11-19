package com.nhnacademy.nhnmart.controller;

import com.nhnacademy.nhnmart.domain.Answer;
import com.nhnacademy.nhnmart.domain.Inquiry;
import com.nhnacademy.nhnmart.domain.dto.AnswerRegisterRequest;
import com.nhnacademy.nhnmart.exception.InquiryNotFoundException;
import com.nhnacademy.nhnmart.exception.ValidationFailedException;
import com.nhnacademy.nhnmart.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/cs/admin")
@RequiredArgsConstructor
public class AdminController {

    private final InquiryRepository inquiryRepository;

    // GET: "/cs/admin" 담당자 메인(답변 안달린 문의 목록)
//    @GetMapping("/")
//    public String adminMain(ModelMap modelMap) {
//        List<Inquiry> unansweredInquiries = inquiryRepository.getUnansweredInquiries();
//
//        modelMap.put("inquiries", unansweredInquiries);
//        return "adminMain";
//    }

    // GET: "/cs/admin/{inquiryId}/answer" 답변 폼
    @GetMapping("/{inquiryId}/answer")
    public String answerForm(@PathVariable long inquiryId, ModelMap modelMap) {
        modelMap.put("inquiryId", inquiryId);
        return "answerRegister";
    }


    // GET: "/cs/admin/{inquiryId}" 문의 상세 조회
    @GetMapping("/{inquiryId}")
    public String getUnansweredInquiry(@PathVariable long inquiryId, ModelMap modelMap) {
        Inquiry inquiry = inquiryRepository.getInquiry(inquiryId);

        if (Objects.isNull(inquiry)) {
            throw new InquiryNotFoundException(String.valueOf(inquiryId));
        }

        modelMap.put("inquiry", inquiry);
        return "inquiryView";
    }

    // POST: "/cs/admin/{inquiryId}/answer" 답변 처리
    @PostMapping("/{inquiryId}/answer")
    public String registerAnswer(@PathVariable long inquiryId,
                                 @Valid @ModelAttribute AnswerRegisterRequest request,
                                 BindingResult bindingResult,
                                 ModelMap modelMap) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        Answer answer = Answer.create(request.getComment(), request.getAdminName());
        Inquiry inquiry = inquiryRepository.addAnswer(inquiryId, answer);

        modelMap.put("inquiry", inquiry);
        return "inquiryView";
    }

    // GET: "/cs/admin?category" 카테고리로 분류
    @GetMapping
    public String findByCategory(@RequestParam(value = "category", required = false) String category,
                                 ModelMap modelMap) {
        List<Inquiry> inquiries = getInquiries(category);
        modelMap.put("inquiries", inquiries);

        return "adminMain";
    }

    private List<Inquiry> getInquiries(String category) {
        if (category == null || Objects.equals(category, "")) {
            return inquiryRepository.getAllInquiries();
        }

        return inquiryRepository.findAllByCategory(category);
    }
}
