package engine.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizAnswer {

    private static final Integer[] emptyArray = {};

    public QuizAnswer() {
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean success;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String feedback;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer[] answer;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Integer[] getAnswer() {
        return answer;
    }

    public void setAnswer(Integer[] answer) {
        this.answer = answer == null ? emptyArray : answer;
    }
}
