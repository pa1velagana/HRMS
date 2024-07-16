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
    HrRepository hrRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public String savingUserCredentials(Hr hr) {
        hr.setPassword(passwordEncoder.encode(hr.getPassword()));
       Optional<Hr> hr1= hrRepository.findByEmail(hr.getEmail());
       if(hr1.isPresent()){
           return "already registered with this email";
       }

        hrRepository.save(hr);
        return "registered successfully";
    }

    @Override
    public String loginCredentials(LoginRequest loginRequest) {
       Optional<Hr> hrUser= hrRepository.findByEmail(loginRequest.getEmail());
       if(hrUser.isPresent()){
          Hr hr1= hrUser.get();
          //passwordEncoder.encode(loginRequest.getPassword())
          if(hr1.getPassword().equals(loginRequest.getPassword())){
              return "login successfull";

          }else{
              return "invalid Password";
          }
       }else{
           return "invalid Email";
       }

    }
}
