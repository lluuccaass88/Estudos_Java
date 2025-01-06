package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.User;
import com.example.login_auth_api.dto.LoginRequestDTO;
import com.example.login_auth_api.dto.RegisterRequestDTO;
import com.example.login_auth_api.dto.ResponseDTO;
import com.example.login_auth_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody LoginRequestDTO body) {
    try {
      return ResponseEntity.ok(authService.login(body.email(), body.password()));
    } catch (BadCredentialsException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("/register")
  public ResponseEntity register(@RequestBody RegisterRequestDTO body) {
    try {
      ResponseDTO responseDTO = this.authService.register(body);
      return ResponseEntity.ok(responseDTO);
    } catch (BadCredentialsException e) {
      return ResponseEntity.badRequest().build();
    }
  }


}
