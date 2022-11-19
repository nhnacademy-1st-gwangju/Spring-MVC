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

    @GetMapping
    public String adminMain(@RequestParam(value = "category", required = false) String category,
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

    @GetMapping("/{inquiryId}/answer")
    public String answerForm(@PathVariable long inquiryId, ModelMap modelMap) {
        modelMap.put("inquiryId", inquiryId);
        return "answerRegister";
    }

    @GetMapping("/{inquiryId}")
    public String getUnansweredInquiry(@PathVariable long inquiryId, ModelMap modelMap) {
        Inquiry inquiry = inquiryRepository.getInquiry(inquiryId);

        if (Objects.isNull(inquiry)) {
            throw new InquiryNotFoundException(String.valueOf(inquiryId));
        }

        modelMap.put("inquiry", inquiry);
        return "inquiryView";
    }

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
}
