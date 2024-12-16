package ru.kata.spring.boot_security.demo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UpdateValidator;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import java.util.Collections;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserValidator userValidator;

    private final UpdateValidator updateValidator;

    private final UserService userService;

    private final RoleService roleService;

    // данное поле необходимо для "запоминания" значения пароля до обновления пользователя
    private String password;

    @Autowired
    public AdminController(UserValidator userValidator, UpdateValidator updateValidator, UserService userService, RoleService roleService) {
        this.userValidator = userValidator;
        this.updateValidator = updateValidator;
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.showAllUsers());
        return "admin/index";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "admin/new";
    }

    @PostMapping()
    public String createNewUser(@ModelAttribute("user") @Valid User user,
                                BindingResult bindingResult) {
        if (user.getRoles().get(0).getName().equals("ROLE_ADMIN")) {
            user.setRoles(Collections.singletonList(roleService.getRole(1)));
        } else {
            user.setRoles(Collections.singletonList(roleService.getRole(2)));
        }
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "admin/new";
        }
        userService.createNewUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/update")
    public String updateUser(@RequestParam("id") int id, Model model) {
        if (userService.getUserById(id).isPresent()) {
            model.addAttribute("user", userService.getUserById(id).get());
            password = userService.getUserById(id).get().getPassword();
            return "admin/update";
        }
        return "redirect:/admin/index";
    }

    @PostMapping("/update")
    public String updateUserOption(@ModelAttribute("user") @Valid User user,
                                   BindingResult bindingResult) {
        if (user.getRoles().get(0).getName().equals("ROLE_ADMIN")) {
            user.setRoles(Collections.singletonList(roleService.getRole(1)));
        } else {
            user.setRoles(Collections.singletonList(roleService.getRole(2)));
        }
        updateValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "admin/update";
        }
        if (user.getPassword().equals(password)) {
            userService.updateUserPart(user);
        } else {
            userService.updateEntireUser(user);
        }
        return "redirect:/admin";
    }

    @GetMapping("/delete")
    public String removeUser(@RequestParam("id") int id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }
}
