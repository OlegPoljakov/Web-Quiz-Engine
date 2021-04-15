package engine.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name="userstorage")
public class UserInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final boolean active = true;

    @NotNull
    @Size(min = 5)
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String roles = "ROLE_ADMIN";

    @NotNull
    @Pattern(regexp = ".+@.+\\..+")
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }
}
