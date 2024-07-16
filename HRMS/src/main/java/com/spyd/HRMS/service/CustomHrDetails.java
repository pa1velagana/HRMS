package com.spyd.HRMS.service;

import com.spyd.HRMS.modal.Hr;
import com.spyd.HRMS.repo.HrRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomHrDetails implements UserDetailsService {

    private HrRepository hrRepository;


    public CustomHrDetails(HrRepository hrRepository) {
        this.hrRepository = hrRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String firstname) throws UsernameNotFoundException {

        Optional<Hr> hr = hrRepository.findByEmail(firstname);

        if(!hr.isPresent()) {
            throw new UsernameNotFoundException("user not found with email "+firstname);
        }
        Hr hr1=hr.get();
        List<GrantedAuthority> authorities = new ArrayList<>();

        return new org.springframework.security.core.userdetails.User(hr1.getEmail(),hr1.getPassword(),authorities);
    }

}
