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

    @Autowired
    AbstractAuthService authService;

    @GetMapping("/me")
    public ResponseEntity<String> login(@RequestHeader("Authorization") String token) throws FirebaseAuthException {
        FirebaseTokenDTO tokenDTO = authService.verifyIdToken(token);
        User user = authService.loginOrEntry(tokenDTO);
        ResponseCookie responseCookie = createCookie("token", token);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(user.getEmail());
    }
    }

    @PostMapping("")
    public User insertUser(@RequestBody UserDTO userDTO){
        return userService.addUser(userDTO);
    @DeleteMapping("")
    public void deleteUser(@RequestHeader("Authorization") String token) throws FirebaseAuthException {
        FirebaseTokenDTO tokenDTO = authService.verifyIdToken(token);
        userService.deleteUser(tokenDTO.getEmail());
    }

    @DeleteMapping("/{no}")
    public Long deleteUser(@PathVariable Long no){
        return userService.deleteUser(no);
    }

}
