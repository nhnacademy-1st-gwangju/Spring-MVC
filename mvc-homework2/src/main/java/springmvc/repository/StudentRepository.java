package springmvc.repository;

import springmvc.domain.Student;

public interface StudentRepository {

    boolean exists(long id);
    Student register(String name, String email, int score, String comment);
    Student getStudent(long id);
    Student modify(Student student);
}
