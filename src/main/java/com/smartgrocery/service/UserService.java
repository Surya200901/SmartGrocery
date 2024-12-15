package com.smartgrocery.service;

import com.smartgrocery.model.User;
import com.smartgrocery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Override loadUserByUsername method to integrate with Spring Security
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        
        User foundUser = user.get();
        // Return user with encoded password
        return org.springframework.security.core.userdetails.User
                .withUsername(foundUser.getUsername())
                .password(foundUser.getPassword()) // Store the encoded password
                .roles("USER") // You can add more roles if needed
                .build();
    }

    // Method to encode password during registration
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // Encoding password before saving
        return userRepository.save(user);
    }

    // Method to find user by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // One-time password encoding logic
    public void encodeExistingPasswords() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (!user.getPassword().startsWith("$2a$")) { // Check if the password is already encoded
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
            }
        }
        System.out.println("Password encoding completed for all users.");
    }
}
