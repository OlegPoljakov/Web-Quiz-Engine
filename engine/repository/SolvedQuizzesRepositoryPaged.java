package engine.repository;

import engine.model.QuizAnswer;
import engine.model.QuizQuestion;
import engine.model.TempObjectSolved;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SolvedQuizzesRepositoryPaged extends PagingAndSortingRepository<TempObjectSolved, Long> {

    @Query("SELECT u FROM solvedquizzes u WHERE u.user_id = :id")
    Page<TempObjectSolved> findAll(@Param("id") Long id, Pageable pageable);

}

