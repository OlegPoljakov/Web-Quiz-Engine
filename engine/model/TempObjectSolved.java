package engine.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity(name = "solvedquizzes")
public class TempObjectSolved {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    private String datetime;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long user_id;

    private Long question_id;

    public TempObjectSolved() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDateTime() {

        LocalDate date= LocalDate.now(); // Create a date object
        LocalTime time = LocalTime.now();

        this.datetime = date + "T" + time;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public void setQuestion_id(Long question_id) {
        this.question_id = question_id;
    }

    public Long getId() {
        return id;
    }

    @JsonProperty("completedAt")
    public String getTime() {
        return datetime;
    }

    @JsonProperty("id")
    public Long getQuestion_id() {
        return question_id;
    }

    public Long getUser_id() {
        return user_id;
    }
}
