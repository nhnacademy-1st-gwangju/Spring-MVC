package springmvc.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException() {
        super("404: student not found");
    }
}
