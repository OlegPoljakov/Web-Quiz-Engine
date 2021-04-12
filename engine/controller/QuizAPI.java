package engine.controller;

import engine.exceptions.UserNotFoundException;
import engine.model.*;
import engine.service.QuizService;
import engine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@SpringBootApplication
@RestController
@Validated
public class QuizAPI {

    private QuizService quizservice;
    private UserService userservice;

    @Autowired
    public QuizAPI(QuizService quizservice, UserService userservice) {
        this.userservice = userservice;
        this.quizservice = quizservice;
    }

    public QuizAPI() {}

    @PostMapping(path = "/api/quizzes", consumes = "application/json")
    public QuizQuestion CreateQuiz(@Valid @RequestBody QuizQuestion inputQuestion, Authentication auth) {
        inputQuestion.setUser(userservice.findByEmail(auth.getName()));

        //Create Option objects and linking them to QuizQuestion
        List<String> options = inputQuestion.getOptions();
        Map<String, AnswerOption> mapOption = new HashMap<>();
        for(int i = 0; i < options.size(); i++) {
            mapOption.put("option" + i, new AnswerOption(options.get(i)));
        }
        for(int i = 0; i < options.size(); i++) {
            AnswerOption opt = mapOption.get("option" + i);
            inputQuestion.getOptionlist().add(opt);
        }

        //Create Answer objects and linking them to QuizQuestion
        int[] answers = inputQuestion.getAnswers();
        if (answers.length != 0) {
            Map<String, AnswerAnswer> mapAnswer = new HashMap<>();
            for(int i = 0; i < answers.length; i++) {
                mapAnswer.put("answer" + i, new AnswerAnswer(answers[i]));
            }
            for(int i = 0; i < answers.length; i++) {
                AnswerAnswer answer = mapAnswer.get("answer" + i);
                inputQuestion.getAnswerlist().add(answer);
            }
        }

        quizservice.addQuizToStorage(inputQuestion);
        return inputQuestion;
    }


    @GetMapping(path = "/api/quizzes/{id}")
    public QuizQuestion getQuizQuestion(@PathVariable String id){
        QuizQuestion retval= quizservice.getQuizFromStorage(id);

        List<String> temp = new ArrayList<>();

        for (AnswerOption option : retval.getOptionlist()) {
            temp.add(option.getOption());
        }
        retval.setOptions(temp);
        return retval;
    }


    @GetMapping(path = "/api/quizzes")
    public List<QuizQuestion> CreateQuiz() {
        List<QuizQuestion> allQuestions= quizservice.getAllQuizesFromStorage();
        List<QuizQuestion> output= new ArrayList<>();
        List<String> temp= new ArrayList<>();

        for (QuizQuestion quizequestion : allQuestions) {
            for (AnswerOption answerOption : quizequestion.getOptionlist()) //Option list: [1,2,3,4]
            {
                temp.add(answerOption.getOption());
            }
            for (String xxx: temp){
                quizequestion.getOptions().add(xxx);
            }
            temp.clear();
        }
        return allQuestions;
    }

    @PostMapping(path = "/api/quizzes/{id}/solve")
    @ResponseBody
    public QuizAnswer AnswerQuestion(@Valid @RequestBody QuizAnswer quizAnswer, @PathVariable("id") String id){

        QuizQuestion retval= quizservice.getQuizFromStorage(id);
        List<AnswerAnswer> temp1 = retval.getAnswerlist();
        List<Integer> answers = new ArrayList<>();

        for(int i = 0; i < temp1.size(); i++) {
            answers.add(temp1.get(i).getAnswer());
        }

        List<Integer> temp2 =  Arrays.asList(quizAnswer.getAnswer());

        Collections.sort(answers);
        Collections.sort(temp2);

        if (answers.equals(temp2)){
            quizAnswer.setSuccess(true);
            quizAnswer.setFeedback("Congratulations, you're right!");
        }
        else {
            quizAnswer.setSuccess(false);
            quizAnswer.setFeedback("Wrong answer! Please, try again.");
        }
        return quizAnswer;
    }

    @PostMapping(path = "/actuator/shutdown")
    public String testedMethod(){
        return "This path doesn't need authentication";
    }

    @GetMapping(path = "/api/test")
    public String Hello(){
        return "we are int the test";
    }

    @PostMapping(path = "/api/register")
    public void userRegister(@Valid @RequestBody UserInformation usr) {
        if (userservice.ifUserExists(usr.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        else {
            userservice.addUserToRepository(usr);
        }
    }

    @DeleteMapping(value = "/api/quizzes/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") String id, Principal principal) {
        if (!quizservice.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return quizservice.deleteById(id, principal);
    }
}
