package com.joshroundy.cherry.service;
import com.joshroundy.cherry.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class AccountCleanupService {
    private final UserRepository userRepository;

    public AccountCleanupService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 3 * * *") // Runs daily at 3 AM
    @Transactional
    public void deleteExpiredUnverifiedAccounts() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(1);
        userRepository.deleteUnverifiedWithExpiredToken(cutoff);
    }
}