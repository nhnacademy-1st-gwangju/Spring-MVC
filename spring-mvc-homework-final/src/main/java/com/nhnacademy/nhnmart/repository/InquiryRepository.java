package com.nhnacademy.nhnmart.repository;

import com.nhnacademy.nhnmart.domain.Answer;
import com.nhnacademy.nhnmart.domain.Inquiry;

import java.util.List;

public interface InquiryRepository {
    boolean exists(long id);
    Inquiry registerInquiry(String title, String category, String content, String author, List<String> uploadFileDirs);
    Inquiry getInquiry(long id);
    List<Inquiry> getAllInquiries();
    Inquiry addAnswer(long id, Answer answer);
    List<Inquiry> getUnansweredInquiries();
    List<Inquiry> findAllByCategory(String category);
}
