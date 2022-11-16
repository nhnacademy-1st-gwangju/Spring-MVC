package springmvc.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDto {
    private long id;
    private String name;
    private String email;
    private int score;
    private String comment;
}
