package guru.ga.rangiffler.service;

import guru.ga.rangiffler.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;


public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
