package engine.repository;


import engine.model.QuizAnswer;
import engine.model.QuizQuestion;
import engine.model.TempObjectSolved;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolvedQuizzesRepository extends CrudRepository<TempObjectSolved, Long> { }