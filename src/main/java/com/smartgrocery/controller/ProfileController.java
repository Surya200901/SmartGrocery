package com.smartgrocery.controller;
import com.smartgrocery.model.UserProfile;
import com.smartgrocery.model.User;
import com.smartgrocery.repository.UserProfileRepository;
import com.smartgrocery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.security.Principal;
import java.util.Optional;
@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private UserRepository userRepository;
    // View user profile
    @GetMapping("")
    public String viewProfile(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Redirect to login if no user is logged in
        }
        String username = principal.getName();  // Get the username from the Principal
        Optional<User> userOpt = userRepository.findByUsername(username);  // Fetch User by username
        if (userOpt.isPresent()) {
            Long userId = userOpt.get().getId();  // Extract userId from the User
            UserProfile userProfile = userProfileRepository.findByUserId(userId);  // Fetch UserProfile by userId
            if (userProfile == null) {
                UserProfile newUserProfile = new UserProfile();
                newUserProfile.setFullName(username); // Set full name as username
                newUserProfile.setUsername(username); // Set username explicitly
                newUserProfile.setUserId(userId.intValue());  // Set the userId
                userProfileRepository.save(newUserProfile);  // Save the new profile
                model.addAttribute("profile", newUserProfile);  // Add to model
            } else {
                model.addAttribute("profile", userProfile);  // Add to model if profile exists
            }
            return "profile";  // Return profile view
        } else {
            return "redirect:/login";  // Redirect to login if User is not found
        }
    }
    // Edit profile
    @GetMapping("/edit")
    public String editProfile(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Redirect if the user is not logged in
        }
        String username = principal.getName(); // Get the logged-in user's username
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            Long userId = userOpt.get().getId(); // Fetch the user ID
            UserProfile userProfile = userProfileRepository.findByUserId(userId);
            if (userProfile == null) {
                userProfile = new UserProfile(); // Create a new profile if none exists
                userProfile.setUsername(username);
                userProfile.setUserId(userId.intValue());
            }
            model.addAttribute("profile", userProfile); // Add the profile to the model
            return "editProfile"; // Return the view name for Thymeleaf
        } else {
            return "redirect:/login"; // Redirect to login if the user is not found
        }
    }
    // Save or update user profile
    @PostMapping
    public String saveOrUpdateProfile(@ModelAttribute UserProfile userProfile, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Redirect if the user is not logged in
        }
        String username = principal.getName(); // Get username from principal
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            Long userId = userOpt.get().getId();  // Use Long for consistency with User ID
            userProfile.setFullName(username); // Set full_name as username
            userProfile.setUsername(username); // Set the username explicitly
            userProfile.setUserId(userId.intValue()); // Set the user_id
            userProfileRepository.save(userProfile); // Save or update profile
            return "redirect:/profile"; // Redirect to the profile page after saving
        } else {
            return "redirect:/login"; // Redirect to login if User is not found
        }
    }
    // Delete user profile
    @DeleteMapping
    public ResponseEntity<String> deleteProfile(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized access!"); // Unauthorized if no principal
        }
        String username = principal.getName();
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            Long userId = userOpt.get().getId();  // Use Long
            userProfileRepository.deleteByUserId(userId); // Delete profile by user_id
            return ResponseEntity.ok("Profile deleted successfully!");
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }
}