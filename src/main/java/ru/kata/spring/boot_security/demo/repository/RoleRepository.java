package ru.kata.spring.boot_security.demo.repository;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

public interface RoleRepository {
    Role getRole(int id);
}
