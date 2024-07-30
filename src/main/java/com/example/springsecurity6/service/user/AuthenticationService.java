package com.example.springsecurity6.service.user;

import com.example.springsecurity6.dto.user.AuthenticationRequest;
import com.example.springsecurity6.dto.user.AuthenticationResponse;
import com.example.springsecurity6.dto.user.RegistrationRequest;
import com.example.springsecurity6.email.EmailService;
import com.example.springsecurity6.email.EmailTemplate;
import com.example.springsecurity6.model.ActivationCode;
import com.example.springsecurity6.model.User;
import com.example.springsecurity6.model.role.Role;
import com.example.springsecurity6.model.token.Token;
import com.example.springsecurity6.model.token.TokenType;
import com.example.springsecurity6.repository.ActivationCodeRepository;
import com.example.springsecurity6.repository.TokenRepository;
import com.example.springsecurity6.repository.UserRepository;
import com.example.springsecurity6.security.JwtService;
import com.example.springsecurity6.util.EmailSenderUtil;
import com.example.springsecurity6.util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import static com.example.springsecurity6.util.IpUtil.getClientIp;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final ActivationCodeRepository activationCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailSenderUtil emailSenderUtil;
    private final TokenUtil tokenUtil;
    public void register(RegistrationRequest request,HttpServletRequest servletRequest) {
        String ipAddress = getClientIp(servletRequest);
        User user = User.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .enabled(false)
                .accountLocked(false)
                .ipAddress(ipAddress)
                .build();
        userRepository.save(user);
        emailSenderUtil.sendValidationEmail(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var claims = new HashMap<String,Object>();
        User user = (User) auth.getPrincipal();
        claims.put("fullName",user.getName());

        String accessToken = jwtService.generateToken(claims,user);
        String refreshToken = jwtService.generateRefreshToken(user);
        tokenUtil.revokeAllUserTokens(user);
        tokenUtil.saveUserToken(user,accessToken,refreshToken);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void activateAccount(String code) {
        ActivationCode activationCode = activationCodeRepository.findByCode(code)
                .orElseThrow(()->new EntityNotFoundException("Code not found"));
        if (LocalDateTime.now().isAfter(activationCode.getExpiresAt())) {
            emailSenderUtil.sendValidationEmail(activationCode.getUser());
            throw new IllegalStateException("Activation code has been expired!");
        }
        User user = userRepository.findByEmail(activationCode.getUser().getEmail())
                .orElseThrow(()->new UsernameNotFoundException("User not found by email"));
        user.setEnabled(true);
        activationCode.setValidatedAt(LocalDateTime.now());
        userRepository.save(user);
        activationCodeRepository.save(activationCode);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader==null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail!=null) {
            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow(()->new UsernameNotFoundException("User not found by email"));
            if (jwtService.isTokenValid(refreshToken,user)) {
                var accessToken = jwtService.generateToken(user);
                tokenUtil.revokeAllUserTokens(user);
                tokenUtil.saveUserToken(user,accessToken,refreshToken);
                var authResponse = AuthenticationResponse.builder()
                        .refreshToken(refreshToken)
                        .accessToken(accessToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(),authResponse);
            }
        }
    }
}
