package springmvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import springmvc.Base;
import springmvc.repository.StudentRepository;
import springmvc.repository.StudentRepositoryImpl;
import springmvc.repository.UserRepository;
import springmvc.repository.UserRepositoryImpl;

@Configuration
@ComponentScan(basePackageClasses = Base.class,
    excludeFilters = { @ComponentScan.Filter(Controller.class)})
public class RootConfig {

    @Bean
    public UserRepository userRepository() {
        UserRepository userRepository = new UserRepositoryImpl();
        userRepository.addUser("admin", "12345");

        return userRepository;
    }

    @Bean
    public StudentRepository studentRepository() {
        StudentRepository studentRepository = new StudentRepositoryImpl();
        studentRepository.register("김학생", "kim.student@nhnacademy.com", 100, "훌륭");

        return studentRepository;
    }
}
