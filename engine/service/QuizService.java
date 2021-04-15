package engine.service;

import engine.exceptions.QuestionNotFoundException;
import engine.model.QuizQuestion;
import engine.model.UserInformation;
import engine.repository.QuizRepository;
import engine.repository.QuizRepositoryPaged;
import engine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuizRepositoryPaged quizRepositoryPaged;

    @Autowired
    public QuizService(QuizRepository quizRepository,
                       UserRepository userRepository,
                       QuizRepositoryPaged quizRepositoryPaged) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.quizRepositoryPaged = quizRepositoryPaged;
    }

    @Transactional
    public void addQuizToStorage(QuizQuestion quizquestion) {
        quizRepository.save(quizquestion);
    }

    public QuizQuestion getQuizFromStorage(String id){
        Optional<QuizQuestion> requiredQuize = quizRepository.findById( Long.parseLong(id, 10));
        if (requiredQuize.isEmpty()) {
            throw new QuestionNotFoundException();
        } else {
            return requiredQuize.get();
        }
    }

    public List<QuizQuestion> getAllQuizesFromStorage(){
        List<QuizQuestion> output = new ArrayList<>();
        quizRepository.findAll().forEach(quizquestion -> {
                output.add(quizquestion);
        });
        return output;
    }

    public boolean existsById(String id) {
        Optional<QuizQuestion> requiredQuize = quizRepository.findById( Long.parseLong(id, 10));
        if (requiredQuize.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }


    public ResponseEntity<String> deleteById(String id, Principal principal) {
        UserInformation user = userRepository.getUserByEmail(principal.getName());
        Optional<QuizQuestion> optionalQuiz = quizRepository.findById(Long.parseLong(id, 10));
        if (optionalQuiz.isEmpty()) {
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        }
        QuizQuestion quiz = optionalQuiz.get();
        int x = quiz.getUser().getId();
        int y = user.getId();
        if (quiz.getUser().getId() == user.getId()) {
            quizRepository.delete(quiz);
            return new ResponseEntity<String>("Successful", HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<String>("Forbidden", HttpStatus.FORBIDDEN);
        }
    }

    public  Page<QuizQuestion> getAllQuizesPaged(Integer pageNo, Integer pageSize, String sortBy)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());

        Page<QuizQuestion> pagedResult = quizRepositoryPaged.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult;
        } else {
            return null;
        }
    }
}
