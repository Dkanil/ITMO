package org.example.lab4.service;

import org.example.lab4.DTO.AuthRequest;
import org.example.lab4.DTO.JwtResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.example.lab4.entity.User;

@Service
public class AuthService {
    private final UserService userService;
    private final JwtCore jwtCore;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserService userService, JwtCore jwtCore, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtCore = jwtCore;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public JwtResponse signUp(AuthRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userService.create(user);

        String jwt = jwtCore.generateToken(user);
        return new JwtResponse(jwt);
    }

    public JwtResponse sigIn(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        UserDetails user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        return new JwtResponse(jwtCore.generateToken(user));
    }

}
