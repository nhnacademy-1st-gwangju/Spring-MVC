package com.nhnacademy.nhnmart.repository;

import com.nhnacademy.nhnmart.domain.Answer;
import com.nhnacademy.nhnmart.domain.Inquiry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InquiryRepositoryImpl implements InquiryRepository {

    private final Map<Long, Inquiry> inquiries = new ConcurrentHashMap<>();

    @Override
    public boolean exists(long id) {
        return inquiries.containsKey(id);
    }

    @Override
    public Inquiry registerInquiry(String title, String category, String content, String author, List<String> uploadFileDirs) {
        Long id = inquiries.keySet()
                .stream()
                .max(Comparator.comparing(Function.identity()))
                .map(l -> l + 1)
                .orElse(1L);

        Inquiry inquiry = Inquiry.create(title, category, content, author);
        inquiry.setId(id);
        List<String> imageFiles = inquiry.getImageFiles();
        imageFiles.addAll(uploadFileDirs);

        inquiries.put(id, inquiry);

        return inquiry;
    }

    @Override
    public Inquiry getInquiry(long id) {
        return exists(id) ? inquiries.get(id) : null;
    }

    @Override
    public List<Inquiry> getAllInquiries() {
        ArrayList<Inquiry> list = new ArrayList<>(this.inquiries.values());
        Collections.reverse(list);

        return list;
    }

    @Override
    public Inquiry addAnswer(long id, Answer answer) {
        Inquiry inquiry = getInquiry(id);
        inquiry.setAnswer(answer);

        inquiries.replace(id, inquiry);

        return inquiry;
    }

    @Override
    public List<Inquiry> getUnansweredInquiries() {
        List<Inquiry> unansweredInquiries = inquiries.values().stream()
                .filter(i -> i.getAnswer() == null)
                .collect(Collectors.toList());

        Collections.reverse(unansweredInquiries);

        return unansweredInquiries;
    }

    @Override
    public List<Inquiry> findAllByCategory(String category) {
        List<Inquiry> byCategory = inquiries.values().stream()
                .filter(i -> i.getCategory().equals(category))
                .collect(Collectors.toList());

        Collections.reverse(byCategory);

        return byCategory;
    }
}
