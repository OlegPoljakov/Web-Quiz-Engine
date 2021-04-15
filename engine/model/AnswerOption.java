package engine.model;

import javax.persistence.*;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name="options")
public class AnswerOption {

    private static final AtomicInteger count = new AtomicInteger(0);

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String Option;

    public AnswerOption() {
    }

    public AnswerOption(String answerOption) {
        this.Option = answerOption;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOption() {
        return Option;
    }

    public void setOption(String option) {
        this.Option = option;
    }
}
