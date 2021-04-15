package engine.service;

import engine.exceptions.UserNotFoundException;
import engine.model.UserInformation;
import engine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void addUserToRepository(UserInformation usr){
        usr.setPassword(encoder.encode(usr.getPassword()));
        userRepository.save(usr);
    }

    public boolean ifUserExists(String email) {
        return  userRepository.existsByEmail(email);
    }

    public UserInformation findByEmail(String email){
        Optional<UserInformation> requiredUser = Optional.ofNullable(userRepository.getUserByEmail(email));
        if (requiredUser.isEmpty()) {
            throw new UserNotFoundException();
        } else {
            return requiredUser.get();
        }
    }


}
