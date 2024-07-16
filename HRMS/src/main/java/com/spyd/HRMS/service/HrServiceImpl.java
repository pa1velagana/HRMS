package com.spyd.HRMS.service;

import com.spyd.HRMS.request.LoginRequest;
import com.spyd.HRMS.modal.Hr;
import com.spyd.HRMS.repo.HrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HrServiceImpl implements HrService {

    @Autowired
    private HrRepository hrRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String savingUserCredentials(Hr hr) {
        hr.setPassword(passwordEncoder.encode(hr.getPassword()));
        Optional<Hr> existingHr = hrRepository.findByEmail(hr.getEmail());
        if (existingHr.isPresent()) {
            return "Already registered with this email";
        }
        hrRepository.save(hr);
        return "Registered successfully";
    }

    @Override
    public String loginCredentials(LoginRequest loginRequest) {
        Optional<Hr> hrUser = hrRepository.findByEmail(loginRequest.getEmail());
        if (hrUser.isPresent()) {
            Hr hr = hrUser.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), hr.getPassword())) {
                return "Login successful";
            } else {
                return "Invalid password";
            }
        } else {
            return "Invalid email";
        }
    }
}
