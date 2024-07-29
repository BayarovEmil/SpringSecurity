package com.example.springsecurity6.repository;

import com.example.springsecurity6.model.ActivationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivationCodeRepository extends JpaRepository<ActivationCode,Integer> {
    Optional<ActivationCode> findByCode(String code);
}
