package ru.kata.spring.boot_security.demo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserValidator userValidator;

    private final UserService userService;

    @Autowired
    public AdminController(UserValidator userValidator, UserService userService) {
        this.userValidator = userValidator;
        this.userService = userService;
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
            return "admin/update";
        }
        return "redirect:/admin/index";
    }

    @PostMapping("/update")
    public String updateUserOption(@ModelAttribute("user") @Valid User user,
                                   BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "admin/update";
        }
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/delete")
    public String removeUser(@RequestParam("id") int id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }
}
