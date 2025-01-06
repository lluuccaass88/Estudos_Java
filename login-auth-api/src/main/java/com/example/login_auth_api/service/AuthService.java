package com.example.login_auth_api.service;

import com.example.login_auth_api.domain.User;
import com.example.login_auth_api.dto.RegisterRequestDTO;
import com.example.login_auth_api.dto.ResponseDTO;
import com.example.login_auth_api.infra.security.TokenService;
import com.example.login_auth_api.repositories.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final TokenService tokenService;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.tokenService = tokenService;
  }

  public ResponseDTO login(String email, String password) {
    User user = this.userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    if (passwordEncoder.matches(password, user.getPassword())) {
      String token = this.tokenService.generateToken(user);
      return new ResponseDTO(user.getName(), token);
    } else {
      throw new BadCredentialsException("Invalid email or password");
    }
  }

  public ResponseDTO register(RegisterRequestDTO user) {
    Optional<User> userData = this.userRepository.findByEmail(user.email());

    if (userData.isEmpty()) {
      User newUser = new User();
      newUser.setName(user.name());
      newUser.setEmail(user.email());
      newUser.setPassword(passwordEncoder.encode(user.password()));

      this.userRepository.save(newUser);

      String token = this.tokenService.generateToken(newUser);

      return new ResponseDTO(newUser.getName(), token);
    } else {
      throw new BadCredentialsException("Email already in use");
    }

  }

}
