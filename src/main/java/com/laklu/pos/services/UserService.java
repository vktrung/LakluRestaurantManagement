package com.laklu.pos.services;

import com.laklu.pos.dataObjects.request.NewUser;
import com.laklu.pos.entities.User;
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
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username).orElseThrow();
        return new UserPrincipal(user);
    }

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
}
