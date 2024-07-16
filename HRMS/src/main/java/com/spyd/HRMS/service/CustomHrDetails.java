package com.spyd.HRMS.service;

import com.spyd.HRMS.modal.Hr;
import com.spyd.HRMS.repo.HrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Import SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomHrDetails implements UserDetailsService {

    private final HrRepository hrRepository;

    @Autowired
    public CustomHrDetails(HrRepository hrRepository, PasswordEncoder passwordEncoder) {
        this.hrRepository = hrRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Hr> optionalHr = hrRepository.findByEmail(email);

        Hr hr = optionalHr.orElseThrow(() ->
                new UsernameNotFoundException("User not found with email: " + email));

        return buildUserDetails(hr);
    }

    private UserDetails buildUserDetails(Hr hr) {
        return new org.springframework.security.core.userdetails.User(
                hr.getEmail(),
                hr.getPassword(),
                mapAuthorities(hr.getRoles()) // Map roles from Hr entity
        );
    }

    private Collection<? extends GrantedAuthority> mapAuthorities(Set<String> roles) {
        if (roles == null) {
            return Collections.emptyList();
        }
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Create SimpleGrantedAuthority objects
                .collect(Collectors.toList());
    }
}
