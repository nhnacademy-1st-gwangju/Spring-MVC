package springmvc.repository;

import springmvc.domain.Student;
import springmvc.exception.StudentNotFoundException;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class StudentRepositoryImpl implements StudentRepository {

    private final Map<Long, Student> students = new ConcurrentHashMap<>();

    @Override
    public boolean exists(long id) {
        return students.containsKey(id);
    }

    @Override
    public Student register(String name, String email, int score, String comment) {
        Long id = students.keySet()
                .stream()
                .max(Comparator.comparing(Function.identity()))
                .map(l -> l + 1)
                .orElse(1L);

        Student student = Student.create(name, email, score, comment);
        student.setId(id);

        students.put(id, student);

        return student;
    }

    @Override
    public Student getStudent(long id) {
        return exists(id) ? students.get(id) : null;
    }

    @Override
    public void modify(Student student) {
        Student dbStudent = getStudent(student.getId());
        if (Objects.isNull(dbStudent)) {
            throw new StudentNotFoundException();
        }

        dbStudent.setName(student.getName());
        dbStudent.setEmail(student.getEmail());
        dbStudent.setScore(student.getScore());
        dbStudent.setComment(student.getComment());

        students.replace(student.getId(), dbStudent);
    }
}
