package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("")
    public User getUser(@RequestBody UserDTO userDTO){
        return userService.getUser(userDTO);
    }

    @PostMapping("")
    public User insertUser(@RequestBody UserDTO userDTO){
        return userService.addUser(userDTO);
    }

}
