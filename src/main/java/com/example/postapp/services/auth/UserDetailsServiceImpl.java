package com.example.postapp.services.auth;

import com.example.postapp.domain.models.User;
import com.example.postapp.domain.models.UserDetailsImpl;
import com.example.postapp.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = repository.findByName(name).orElseThrow(() -> {
                    throw new UsernameNotFoundException("User not found");
                }
        );
        return UserDetailsImpl.build(user);
    }
}
