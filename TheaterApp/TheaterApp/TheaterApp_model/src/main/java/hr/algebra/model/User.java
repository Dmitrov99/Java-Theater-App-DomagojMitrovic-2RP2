package hr.algebra.model;

import java.util.Objects;

public class User extends BaseClass<User> {
//todo passwordhash


    private String username;
    private String passwordHash;
    private Role role;


    public User(String username, String passwordHash, Role role) {
        super(null);
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }


    public User(Long id, String username, String passwordHash, Role role) {
        super(id);
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    @Override
    public int compareTo(User other) {
        return this.username.compareTo(other.getUsername());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(passwordHash, user.passwordHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, passwordHash);
    }

    @Override
    public String toString() {
        return username;
    }
}