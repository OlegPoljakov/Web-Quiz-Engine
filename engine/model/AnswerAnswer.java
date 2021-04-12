package engine.model;

import javax.persistence.*;

@Entity
@Table(name="answers")
public class AnswerAnswer {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "quizquestionid")
    private QuizQuestion quizquestion;

    private Integer answer;

    public AnswerAnswer() {
    }

    public AnswerAnswer(Integer answer) {
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }
}