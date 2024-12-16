package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(int id) {
        return userRepository.getUserById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> showAllUsers() {
        return userRepository.showAllUsers();
    }

    @Override
    @Transactional
    public void createNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.createNewUser(user);
    }

    @Override
    @Transactional
    public void updateEntireUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.updateUser(user);
    }

    @Override
    @Transactional
    public void updateUserPart(User user) {
        userRepository.updateUser(user);
    }

    @Override
    @Transactional
    public void removeUserById(int id) {
        userRepository.removeUserById(id);
    }
}
