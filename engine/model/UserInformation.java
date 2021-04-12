package engine.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import engine.model.QuizQuestion;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="userstorage")
public class UserInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    //@Column
    private final boolean active = true;

    //@Column
    //@Size(max = 300)
    @NotNull
    @Size(min = 5)
    //@Pattern(regexp = "\\S+")
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    //@Column
    private final String roles = "ROLE_ADMIN";

    @NotNull
    //@Column
    @Pattern(regexp = ".+@.+\\..+")
    private String email;

    //@OneToMany(mappedBy="usr")
    //private Set<QuizQuestion> questions  = new HashSet<>();

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

    /*
    public Set<QuizQuestion> getQuestions() { return questions; }

    public void setQuestions(Set<QuizQuestion> questions) {
        this.questions = questions;
    }
    */
}
