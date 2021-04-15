package engine.service;

import engine.model.TempObjectSolved;
import engine.repository.SolvedQuizzesRepository;
import engine.repository.SolvedQuizzesRepositoryPaged;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class SolvedQuizzesService {

    @Autowired
    SolvedQuizzesRepository solvedquizzesrepository;

    @Autowired
    UserService userservice;

    @Autowired
    SolvedQuizzesRepositoryPaged solvedquizzesrepositorypaged;

    @Transactional
    public void addSolvedQuizzToStorage(TempObjectSolved solvedquizz) {
        solvedquizzesrepository.save(solvedquizz);
    }


    public Page<TempObjectSolved> getAllQuizesPaged(Integer pageNo, Integer pageSize, String sortBy, Authentication auth)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

        Page<TempObjectSolved> pagedResult = solvedquizzesrepositorypaged.findAll((long) userservice.findByEmail(auth.getName()).getId(), paging);

        if(pagedResult.hasContent()) {
            return pagedResult;
        } else {
            return null;
        }
    }
}

