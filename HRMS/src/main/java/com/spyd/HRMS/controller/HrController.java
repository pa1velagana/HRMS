package com.spyd.HRMS.controller;

import com.spyd.HRMS.configuration.JwtTokenProvider;
import com.spyd.HRMS.request.LoginRequest;
import com.spyd.HRMS.modal.Hr;
import com.spyd.HRMS.response.AuthResponse;
import com.spyd.HRMS.service.CustomHrDetails;
import com.spyd.HRMS.service.HrService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;

@RestController
@RequestMapping("/hrms")
@Validated
public class HrController {

    private final HrService hrService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomHrDetails customHrDetails;

    @Autowired
    public HrController(HrService hrService, PasswordEncoder passwordEncoder,
                        JwtTokenProvider jwtTokenProvider, CustomHrDetails customHrDetails) {
        this.hrService = hrService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customHrDetails = customHrDetails;
    }

    @PostMapping("/save")
    public ResponseEntity<String> createUser(@Valid @RequestBody Hr user) {
        // Ensure the user has at least the ROLE_USER
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(new HashSet<>(Collections.singletonList("ROLE_USER")));
        }
        String response = hrService.savingUserCredentials(user);
        if ("Already registered with this email".equals(response)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticate(loginRequest.getEmail(), loginRequest.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(authentication);
            AuthResponse authResponse = new AuthResponse(token, true, "Login successful");
            return ResponseEntity.ok(authResponse);
        } catch (BadCredentialsException e) {
            AuthResponse authResponse = new AuthResponse();
            authResponse.setAuthenticated(false);
            authResponse.setMessage("Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
        }
    }

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = customHrDetails.loadUserByUsername(email);
        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
