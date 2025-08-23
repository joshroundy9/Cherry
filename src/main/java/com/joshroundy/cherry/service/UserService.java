package com.joshroundy.cherry.service;

import com.joshroundy.cherry.dataobject.auth.UserResponseDTO;
import com.joshroundy.cherry.dataobject.entity.UserEntity;
import com.joshroundy.cherry.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username is not valid"));
    }
    public UserResponseDTO loadUserEntityByUsername(String username) {
        var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username is not valid"));
        return UserResponseDTO.builder()
                .userID(user.getUserID())
                .username(user.getUsername())
                .email(user.getEmail())
                .isEmailVerified(user.getIsEmailVerified())
                .weight(user.getWeight())
                .startingWeight(user.getStartingWeight())
                .createdTS(user.getCreatedTS())
                .build();
    }
    public UserResponseDTO updateUserWeight(Integer userID, Double weight) {
        var userEntity = userRepository.findByUserID(userID);
        userEntity.setWeight(weight);
        if (userEntity.getStartingWeight() == null) {
            userEntity.setStartingWeight(weight);
        }
        if (userEntity.getCreatedTS() == null) {
            userEntity.setCreatedTS(new Timestamp(System.currentTimeMillis()));
        }
        userRepository.save(userEntity);
        return UserResponseDTO.builder()
                .userID(userEntity.getUserID())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .isEmailVerified(userEntity.getIsEmailVerified())
                .weight(userEntity.getWeight())
                .startingWeight(userEntity.getStartingWeight())
                .createdTS(userEntity.getCreatedTS())
                .build();
    }
    public void deleteUserAccount(Integer userID) {
        var userEntity = userRepository.findByUserID(userID);
        if (userEntity == null) {
            throw new RuntimeException("User not found");
        }
        userRepository.delete(userEntity);
    }
}
