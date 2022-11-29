package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User getUser(Long userEntryNo) {
        Optional<User> optionalUser = userRepository.findById(userEntryNo);
        if (!optionalUser.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.");
        return optionalUser.get();
    }

    public User addUser(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(userDTO.getEmail());
        if (optionalUser.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하는 유저입니다.");

        User user = User.builder()
                .userEntryNo(userDTO.getUserEntryNo())
                .email(userDTO.getEmail())
                .registeredDate(new Date())
                .build();

        return userRepository.save(user);
    }

    public Long deleteUser(Long no) {
        userRepository.deleteById(no);
        return no;
    }
}
