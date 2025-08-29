package com.team4.hospital_system.service.Impl;

import com.team4.hospital_system.model.User;
import com.team4.hospital_system.model.UserPrinciple;
import com.team4.hospital_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;
    @Autowired
    public MyUserDetailsService(UserRepository userRepo){
        this.userRepo=userRepo;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByEmail(email);
        if(user==null){
            System.out.println("User not Found");
            throw new UsernameNotFoundException("user not found");
        }
        return new UserPrinciple(user);
    }
}
