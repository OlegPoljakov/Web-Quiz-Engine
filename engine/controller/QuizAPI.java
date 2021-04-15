package engine.controller;

import engine.model.*;
import engine.service.QuizService;
import engine.service.SolvedQuizzesService;
import engine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
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

    @Autowired
    QuizService quizservice;

    @Autowired
    UserService userservice;

    @Autowired
    SolvedQuizzesService solvedquizzesservice;

    @Autowired
    public QuizAPI() {}

    @PostMapping(path = "/api/quizzes", consumes = "application/json")
    public QuizQuestion CreateQuiz(@Valid @RequestBody QuizQuestion inputQuestion, Authentication auth) {
        System.out.println("IN POST /api/quizzes");

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
        System.out.println("OUT POST /api/quizzes");
        return inputQuestion;
    }


    @GetMapping(path = "/api/quizzes/{id}")
    public QuizQuestion getQuizQuestion(@PathVariable String id){
        System.out.println("IN GET /api/quizzes/{id}");
        QuizQuestion retval= quizservice.getQuizFromStorage(id);

        List<String> temp = new ArrayList<>();

        for (AnswerOption option : retval.getOptionlist()) {
            temp.add(option.getOption());
        }
        retval.setOptions(temp);
        System.out.println("OUT GET /api/quizzes/{id}");
        return retval;
    }


    @PostMapping(path = "/api/quizzes/{id}/solve")
    @ResponseBody
    public QuizAnswer AnswerQuestion(@Valid @RequestBody QuizAnswer quizAnswer, @PathVariable("id") String id, Authentication auth){
        System.out.println("IN POST /api/quizzes/{id}/solve");
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

            TempObjectSolved newsolvedbyuser = new TempObjectSolved();
            newsolvedbyuser.setUser_id((long) userservice.findByEmail(auth.getName()).getId());
            newsolvedbyuser.setDateTime();
            newsolvedbyuser.setQuestion_id(Long.parseLong(id, 10));

            solvedquizzesservice.addSolvedQuizzToStorage(newsolvedbyuser);
        }
        else {
            quizAnswer.setSuccess(false);
            quizAnswer.setFeedback("Wrong answer! Please, try again.");
        }
        System.out.println("OUT POST /api/quizzes/{id}/solve");
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
        System.out.println("IN POST /api/register");
        if (userservice.ifUserExists(usr.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        else {
            userservice.addUserToRepository(usr);
        }
        System.out.println("OUT POST /api/register");
    }

    @DeleteMapping(value = "/api/quizzes/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") String id, Principal principal) {
        System.out.println("IN DELETE /api/quizzes/{id}");
        if (!quizservice.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        System.out.println("OUT DELETE /api/quizzes/{id}");
        return quizservice.deleteById(id, principal);
    }


    @GetMapping(path = "/api/quizzes")
    @ResponseBody
    public Page<QuizQuestion> getAllEmployees(
            @RequestParam(defaultValue = "0") String page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy)
    {
        System.out.println("IN GET /api/quizzes");
        Page<QuizQuestion> list = quizservice.getAllQuizesPaged(Integer.parseInt(page), pageSize, sortBy);
        List<String> temp= new ArrayList<>();
        if (list != null) {
            for (QuizQuestion quizequestion : list) {
                for (AnswerOption answerOption : quizequestion.getOptionlist()) //Option list: [1,2,3,4]
                {
                    temp.add(answerOption.getOption());
                }
                for (String xxx: temp){
                    quizequestion.getOptions().add(xxx);
                }
                temp.clear();
            }
            return  list;
        }
        else {
            return Page.empty();
        }
    }

    @GetMapping(path = "/api/quizzes/completed")
    @ResponseBody
    public Page<TempObjectSolved> getAllEmployees(
            @RequestParam(defaultValue = "0") String page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "datetime") String sortBy,
            Authentication auth)
    {
        Page<TempObjectSolved> list = solvedquizzesservice.getAllQuizesPaged(Integer.parseInt(page), pageSize, sortBy, auth);
        if (list != null) {
            return  list;
        }
        else {
            return Page.empty();
        }
    }
}

