package com.team4.hospital_system.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class UserPrinciple implements UserDetails {

    private final Optional<User> user;

    public UserPrinciple(Optional<User> user){
        this.user = user;
    }

    public Optional<User> getUser(){
        return this.user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // This converts your Role enum into a GrantedAuthority object that Spring Security understands.
        return List.of(new SimpleGrantedAuthority(user.get().getRole().name()));
    }

    @Override
    public String getPassword() {
        // This must return the field that holds the hashed password.
        return user.get().getHashedPassword();
    }

    @Override
    public String getUsername() {
        return user.get().getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
