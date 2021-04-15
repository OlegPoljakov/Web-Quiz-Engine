package engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Entity
@RestController
@Table(name = "quizestorage")
public class QuizQuestion {

    private static final AtomicInteger count = new AtomicInteger(0);
    private static final List<Integer> emptyList = new ArrayList<>();
    private static final int[] emptyArray = {};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    @NotEmpty
    private String title;


    @NotBlank
    @NotEmpty
    private String text;

    @Size(min = 2)
    @Transient
    private List<String> options = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Transient
    private int[] answer;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "quizquestionid", referencedColumnName = "id")
    private List<AnswerOption> optionlist = new ArrayList<AnswerOption>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "quizquestionid", referencedColumnName = "id")
    private List<AnswerAnswer> answerlist = new ArrayList<AnswerAnswer>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userinformation_id", nullable=false)
    private UserInformation usr;


    public QuizQuestion() {
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("options")
    public List<String> getOptions() {
        return options;
    }

    @JsonProperty("answers")
    @JsonIgnore
    public int[] getAnswers() {
        if (answer == null) {
            return emptyArray;
        }
        else {
            return answer;
        }
    }

    @JsonIgnore
    public UserInformation getUser() { return usr; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setAnswers(int[] answers) {
        this.answer = answers;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<AnswerOption> getOptionlist() {
        return optionlist;
    }

    public List<AnswerAnswer> getAnswerlist() {
        return answerlist;
    }

    public void setOptionlist(List<AnswerOption> optionlist) {
        this.optionlist = optionlist;
    }

    public void addAnswerOption(AnswerOption option) {
        optionlist.add(option);
    }

    public void setUser(UserInformation user) {
        this.usr = user;
    }
}
