package com.talentotech.energia.service;

import com.talentotech.energia.dto.LoginRequest;
import com.talentotech.energia.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.talentotech.energia.repository.UserRepository;
import com.talentotech.energia.exception.ResourceNotFoundException;
import com.talentotech.energia.service.JwtService;
import com.talentotech.energia.model.Token;
import com.talentotech.energia.repository.TokenRepository;

@Service
public class UserService {
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
            PasswordEncoder passwordEncoder, JwtService jwtService, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
    }

    public User crearUsuario(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User update(long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (userDetails.getUsername() != null &&
                !userDetails.getUsername().trim().isEmpty()) {
            user.setUsername(userDetails.getUsername());
        }
        if (userDetails.getEmail() != null &&
                !userDetails.getEmail().trim().isEmpty()) {
            user.setEmail(userDetails.getEmail());
        }
        if (userDetails.getPassword() != null &&
                !userDetails.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        if (userDetails.getRole() != null)
            user.setRole(userDetails.getRole());

        return userRepository.save(user);
    }

    public String login(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("usuario no encontrado");
        }
        User user = optionalUser.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("Contraseña incorrecta");
        }
        String jwt = jwtService.generateToken(user);

        Token tokenEntity = new Token();
        tokenEntity.setUser(user);
        tokenEntity.setToken(jwt);
        tokenRepository.save(tokenEntity);

        return jwt;

    }

}
