package springmvc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springmvc.domain.Student;
import springmvc.domain.StudentModifyRequest;
import springmvc.domain.StudentRegisterRequest;
import springmvc.domain.StudentResponseDto;
import springmvc.repository.StudentRepository;

@RestController
public class StudentRestController {

    private final StudentRepository studentRepository;

    public StudentRestController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<StudentResponseDto> getStudent(@PathVariable("studentId") long studentId) {
        Student student = studentRepository.getStudent(studentId);
        StudentResponseDto responseDto = new StudentResponseDto(student.getId(), student.getName(), student.getEmail(), student.getScore(), student.getComment());
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/students")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponseDto registerStudent(@RequestBody StudentRegisterRequest request) {
        Student student = studentRepository.register(request.getName(), request.getEmail(), request.getScore(), request.getComment());
        return new StudentResponseDto(student.getId(), student.getName(), student.getEmail(), student.getScore(), student.getComment());
    }

    @PutMapping("/students/{studentId}")
    public ResponseEntity<Student> modify(@PathVariable("studentId") long studentId,
                                    @RequestBody StudentModifyRequest request) {
        Student student = studentRepository.getStudent(studentId);

        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setScore(request.getScore());
        student.setComment(request.getComment());

        studentRepository.modify(student);

        return ResponseEntity.ok(student);
    }
}
