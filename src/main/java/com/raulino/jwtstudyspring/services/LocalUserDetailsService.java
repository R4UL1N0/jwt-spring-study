package com.raulino.jwtstudyspring.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import com.raulino.jwtstudyspring.models.LocalUser;
import com.raulino.jwtstudyspring.repositories.LocalUserRepository;
import com.raulino.jwtstudyspring.security.LocalUserDetails;

@Service
public class LocalUserDetailsService implements UserDetailsManager {

    private final LocalUserRepository userRepository;

    LocalUserDetailsService(LocalUserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createUser'");
    }

    @Override
    public void updateUser(UserDetails user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public void deleteUser(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

    @Override
    public boolean userExists(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'userExists'");
    }
    
}
