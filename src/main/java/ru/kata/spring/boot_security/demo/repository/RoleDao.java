package ru.kata.spring.boot_security.demo.repository;

import ru.kata.spring.boot_security.demo.model.Role;

public interface RoleDao {
    Role getRole(int id);
}
