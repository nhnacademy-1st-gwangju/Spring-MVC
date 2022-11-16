package springmvc.domain;

import lombok.Getter;
import lombok.Setter;

public class User {
    @Getter
    private final String id;

    @Getter
    private final String password;

    @Getter
    @Setter
    private int age;

    @Getter
    @Setter
    private String name;

    public static User create(String id, String password) {
        return new User(id, password);
    }

    private User(String id, String password) {
        this.id = id;
        this.password = password;
    }

    private static final String MASK = "*****";

    public static User constructPasswordMaskedUser(User user) {
        User newUser = User.create(user.getId(), MASK);
        newUser.setAge(user.getAge());
        newUser.setName(user.getName());

        return newUser;
    }

}
