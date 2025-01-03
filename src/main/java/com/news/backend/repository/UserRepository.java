package com.news.backend.repository;

import com.news.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauthIdAndProvider(String oauthId, String provider);
    Optional<User> findByEmail(String email);
}