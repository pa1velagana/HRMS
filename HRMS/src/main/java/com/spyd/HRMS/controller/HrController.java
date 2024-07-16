package com.spyd.HRMS.controller;

import com.spyd.HRMS.configuration.JwtTokenProvider;
import com.spyd.HRMS.request.LoginRequest;
import com.spyd.HRMS.modal.Hr;
import com.spyd.HRMS.response.AuthResponse;
import com.spyd.HRMS.service.CustomHrDetails;
import com.spyd.HRMS.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hrms")
public class HrController {

    @Autowired
    HrService hrService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    CustomHrDetails customHrDetails;

    @PostMapping("/save")
    public ResponseEntity<String> creatingUser(@RequestBody Hr user){
        return new ResponseEntity<>(hrService.savingUserCredentials(user), HttpStatus.CREATED);
    }
//
//    @PostMapping("/login")
//    public ResponseEntity<String> loginCredentials(@RequestBody LoginRequest loginRequest){
//        return new ResponseEntity<>(hrService.loginCredentials(loginRequest),HttpStatus.OK);
//    }


    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        System.out.println(username + " ----- " + password);

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        String token = jwtTokenProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();

        authResponse.setStatus(true);
        authResponse.setJwt(token);


        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customHrDetails.loadUserByUsername(username);

        System.out.println("sign in userDetails - " + userDetails);

        if (userDetails == null) {
            System.out.println("sign in userDetails - null " + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            System.out.println("sign in userDetails - password not match " + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
