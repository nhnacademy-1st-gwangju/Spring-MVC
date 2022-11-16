package springmvc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springmvc.domain.Student;
import springmvc.domain.StudentModifyRequest;
import springmvc.domain.StudentRegisterRequest;
import springmvc.repository.StudentRepository;

import javax.validation.Valid;

@RestController
public class StudentRestController {

    private final StudentRepository studentRepository;

    public StudentRestController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<Student> getStudent(@PathVariable("studentId") long studentId) {
        Student student = studentRepository.getStudent(studentId);
        return ResponseEntity.ok(student);
    }

    @PostMapping("/students")
    @ResponseStatus(HttpStatus.CREATED)
    public Student registerStudent(@Valid @RequestBody StudentRegisterRequest request) {
        return studentRepository.register(request.getName(), request.getEmail(), request.getScore(), request.getComment());
    }

    @PutMapping("/students/{studentId}")
    public ResponseEntity<Student> modify(@PathVariable("studentId") long studentId,
                                    @Valid @RequestBody StudentModifyRequest request) {
        Student student = studentRepository.getStudent(studentId);

        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setScore(request.getScore());
        student.setComment(request.getComment());

        Student modify = studentRepository.modify(student);

        return ResponseEntity.ok(modify);
    }
}
