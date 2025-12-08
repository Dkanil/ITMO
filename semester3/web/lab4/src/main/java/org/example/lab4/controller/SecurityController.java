package org.example.lab4.controller;

import org.example.lab4.DTO.AuthRequest;
import org.example.lab4.DTO.JwtResponse;
import org.example.lab4.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/auth")
class SecurityController {
    private final AuthService authService;

    public SecurityController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public JwtResponse signUp(@RequestBody AuthRequest request) {
        return authService.signUp(request);
    }

    @PostMapping("/sign-in")
    public JwtResponse signIn(@RequestBody AuthRequest request) {
        return authService.sigIn(request);
    }
}
