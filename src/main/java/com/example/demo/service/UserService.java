package com.example.demo.service;

import com.example.demo.consts.UserActiveStatus;
import com.example.demo.entity.User;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    UserRepository userRepository;

    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER, "존재하지 않는 유저입니다."));
    }

    public User getActiveUser(String email) {
        User user = getUser(email);
        if (!user.isActiveUser())
            throw new CustomException(ErrorCode.NOT_CORRECT_USER, "비활성화 유저입니다. 다시 로그인 해주세요");
        return user;
    }

    public User addUser(String email) {
        User user = User.builder()
                .email(email).build();
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElse(null);
    }
}
