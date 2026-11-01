
package com.sakshi.expensetracker.service;

import com.sakshi.expensetracker.dto.RegisterRequest;
import com.sakshi.expensetracker.dto.AuthResponse;
import com.sakshi.expensetracker.entity.User;
import com.sakshi.expensetracker.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email is already registered"
            );
        }

        User user = new User();

        user.setName(request.getName().trim());
        user.setEmail(email);

        // Hash the password before saving
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        // Never accept a role from the registration request
        user.setRole("ROLE_USER");

        User savedUser = userRepository.save(user);

        return new AuthResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                "Registration successful"
        );
    }
}
