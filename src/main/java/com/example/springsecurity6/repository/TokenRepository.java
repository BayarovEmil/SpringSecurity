package com.example.springsecurity6.repository;

import com.example.springsecurity6.model.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Integer> {
    @Query("""
            select token
            from Token token
            where token.revoked=false
            and token.expired=false
            and token.user.id=:userId
            """)
    List<Token> findAllValidTokensByUser(Integer userId);

    Optional<Token> findByToken(String token);
}
