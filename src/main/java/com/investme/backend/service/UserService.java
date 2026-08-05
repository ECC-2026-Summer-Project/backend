package com.investme.backend.service;

import com.investme.backend.dto.LoginRequest;
import com.investme.backend.dto.SignupRequest;
import com.investme.backend.entity.User;
import com.investme.backend.exception.DuplicateUserIdException;
import com.investme.backend.exception.InvalidLoginException;
import com.investme.backend.jwt.JwtProvider;
import com.investme.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.investme.backend.dto.PasswordChangeRequest;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public User signup(SignupRequest request) {

        if (userRepository.existsByUserId(request.getUserId())) {
            throw new DuplicateUserIdException();
        }

        User user = User.builder()
                .userId(request.getUserId())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .cashBalance(10000000L)
                .build();

        userRepository.save(user);

        return user;
    }

    public boolean checkUserId(String userId) {
        return !userRepository.existsByUserId(userId);
    }

    public User login(LoginRequest request) {

        Optional<User> optionalUser =
                userRepository.findByUserId(request.getUserId());

        User user = optionalUser.orElseThrow(
                InvalidLoginException::new
        );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        )) {
            throw new InvalidLoginException();
        }

        String refreshToken =
                jwtProvider.createRefreshToken(user.getUserId());

        user.setRefreshToken(refreshToken);

        userRepository.save(user);

        return user;
    }

    public void changePassword(
            String userId,
            PasswordChangeRequest request
    ) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(InvalidLoginException::new);

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPasswordHash()
        )) {
            throw new InvalidLoginException();
        }

        user.setPasswordHash(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);

    }

    public void logout(String userId){

        User user = userRepository.findByUserId(userId)
                .orElseThrow(InvalidLoginException::new);

        user.setRefreshToken(null);

        userRepository.save(user);

    }

    public void deleteUser(String userId){

        User user = userRepository.findByUserId(userId)
                .orElseThrow(InvalidLoginException::new);

        userRepository.delete(user);

    }

}