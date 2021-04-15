package engine.repository;


import engine.model.QuizQuestion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepositoryPaged extends PagingAndSortingRepository<QuizQuestion, Long> { }