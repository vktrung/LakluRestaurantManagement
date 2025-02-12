package com.laklu.pos.services;

import com.laklu.pos.dataObjects.request.NewUser;
import com.laklu.pos.entities.User;
import com.laklu.pos.exceptions.httpExceptions.NotFoundException;
import com.laklu.pos.repositories.UserRepository;
import com.laklu.pos.validator.RuleValidator;
import com.laklu.pos.validator.UsernameMustBeUnique;
import com.laklu.pos.valueObjects.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User store(NewUser user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword(), passwordEncoder);

        return userRepository.save(newUser);
    }

    // TODO: handle partial update
    public User edit(NewUser user) {
        return null;
    }

    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id);
    }

    public User findOrFail(Integer id) {
        return this.findUserById(id).orElseThrow(NotFoundException::new);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
