package com.joshroundy.cherry.repository;

import com.joshroundy.cherry.dataobject.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByUserID(Integer userID);
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmailVerificationToken(String emailVerificationToken);
    Optional<UserEntity> findByResetPasswordToken(String passwordResetToken);
    Optional<UserEntity> findByEmail(String email);
}
