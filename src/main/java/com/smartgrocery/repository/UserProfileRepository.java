package com.smartgrocery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smartgrocery.model.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByUserId(Long userId);  // Use Long here
    void deleteByUserId(Long userId);  // Use Long here
}
