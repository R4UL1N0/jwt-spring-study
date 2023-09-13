package com.raulino.jwtstudyspring.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import com.raulino.jwtstudyspring.models.LocalUser;
import com.raulino.jwtstudyspring.repositories.LocalUserRepository;
import com.raulino.jwtstudyspring.security.LocalUserDetails;

@Service
public class LocalUserDetailsService implements UserDetailsManager {

    @Autowired
    private LocalUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<LocalUser> opUser = userRepository.findByUsername(username);
        if (!opUser.isPresent()) {
            throw new UsernameNotFoundException("User not found in the DB");
        }

        return new LocalUserDetails(opUser.get());
    }

    @Override
    public void createUser(UserDetails user) {
        
    }

    @Override
    public void updateUser(UserDetails user) {
    }

    @Override
    public void deleteUser(String username) {
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
    
}
