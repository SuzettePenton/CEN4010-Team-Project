package com.example.profile_management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.profile_management.model.Users;
import com.example.profile_management.repository.UsersRepository;

@RestController
@RequestMapping("/profile/users")
public class UserController {
    private final UsersRepository usersRepository;

    public UserController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping("/{username}")
    public Users getUserByUsername(@PathVariable String username) {
        return usersRepository.findById(username).orElse(null);
    }
}