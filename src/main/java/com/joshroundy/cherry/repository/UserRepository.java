package com.joshroundy.cherry.repository;

import com.joshroundy.cherry.dataobject.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByUserID(Integer userID);
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmailVerificationToken(String emailVerificationToken);
    Optional<UserEntity> findByResetPasswordToken(String passwordResetToken);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByDeleteAccountToken(String deleteAccountToken);
    void deleteByUserID(Integer userID);
    @Modifying
    @Query("DELETE FROM UserEntity u WHERE u.isEmailVerified = false AND u.emailVerificationTokenCreatedTS < :cutoff")
    void deleteUnverifiedWithExpiredToken(LocalDateTime cutoff);
}
