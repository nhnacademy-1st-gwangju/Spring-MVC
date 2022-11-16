package springmvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springmvc.domain.Student;
import springmvc.domain.StudentModifyRequest;
import springmvc.exception.StudentNotFoundException;
import springmvc.exception.ValidationFailedException;
import springmvc.repository.StudentRepository;

import javax.validation.Valid;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/student")
public class StudentController {
    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @ModelAttribute("student")
    public Student getStudent(@PathVariable("studentId") long id) {
        Student student = studentRepository.getStudent(id);

        if (Objects.isNull(student)) {
            throw new StudentNotFoundException();
        }

        return student;
    }

    @GetMapping("/{studentId}/")
    public String viewStudent(@ModelAttribute("student") Student student,
                              @PathVariable("studentId") long studentId,
                              ModelMap modelMap) {
        modelMap.put("student", student);
        return "studentView";
    }

    @GetMapping("/{studentId}")
    public String viewStudentHideScore(@ModelAttribute("student") Student student,
                                       @PathVariable("studentId") long studentId,
                                       @RequestParam(defaultValue = "no", required = false) String hideScore,
                                       ModelMap modelMap) {
        if (hideScore.equals("yes")) {
            modelMap.addAttribute("hideScore", hideScore);
        }

        modelMap.put("student", student);
        return "studentViewWithoutScore";
    }

    @GetMapping("/{studentId}/modify")
    public String studentModifyForm(@ModelAttribute("student") Student student,
                                    Model model, @PathVariable long studentId) {
        model.addAttribute("student", student);
        return "studentModify";
    }

    @PostMapping("/{studentId}/modify")
    public String modifyUser(@ModelAttribute("student") Student student,
                             @Valid @ModelAttribute StudentModifyRequest request,
                             BindingResult bindingResult,
                             Model model, @PathVariable long studentId) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setScore(request.getScore());
        student.setComment(request.getComment());

        studentRepository.modify(student);

        model.addAttribute("student", student);
        return "studentView";
    }

    @ExceptionHandler(StudentNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void notFound(StudentNotFoundException ex) {
        log.error("error={}", ex.getMessage());
    }
}
