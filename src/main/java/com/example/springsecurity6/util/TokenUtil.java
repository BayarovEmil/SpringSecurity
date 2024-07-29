package com.example.springsecurity6.util;

import com.example.springsecurity6.model.User;
import com.example.springsecurity6.model.token.Token;
import com.example.springsecurity6.model.token.TokenType;
import com.example.springsecurity6.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenUtil {
    private final TokenRepository tokenRepository;
    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
    }

    public void saveUserToken(User savedUser, String accessToken, String refreshToken) {
        Token token = Token.builder()
                .user(savedUser)
                .token(accessToken)
                .refreshToken(refreshToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

}
