package com.nhnacademy.nhnmart.config;

import com.nhnacademy.nhnmart.Base;
import com.nhnacademy.nhnmart.repository.InquiryRepository;
import com.nhnacademy.nhnmart.repository.InquiryRepositoryImpl;
import com.nhnacademy.nhnmart.repository.UserRepository;
import com.nhnacademy.nhnmart.repository.UserRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

import static com.nhnacademy.nhnmart.domain.RoleUser.ROLE_ADMIN;
import static com.nhnacademy.nhnmart.domain.RoleUser.ROLE_USER;

@Configuration
@ComponentScan(basePackageClasses = Base.class,
        excludeFilters = { @ComponentScan.Filter(Controller.class)})
public class RootConfig {

    @Bean
    public UserRepository userRepository() {
        UserRepositoryImpl userRepository = new UserRepositoryImpl();
        userRepository.addUser("admin", "12345", ROLE_ADMIN, "관리자");
        userRepository.addUser("Ramos", "12345", ROLE_USER, "Ramos");

        return userRepository;
    }

    @Bean
    public InquiryRepository inquiryRepository() {
        InquiryRepositoryImpl inquiryRepository = new InquiryRepositoryImpl();
        inquiryRepository.registerInquiry("Test1", "Praise", "테스트 본문", "Ramos", new ArrayList<>());
        inquiryRepository.registerInquiry("Test2", "Praise", "테스트 본문", "Ramos", new ArrayList<>());

        return inquiryRepository;
    }
}
