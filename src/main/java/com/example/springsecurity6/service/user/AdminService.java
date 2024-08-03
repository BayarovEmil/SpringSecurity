package com.example.springsecurity6.service.user;

import com.example.springsecurity6.model.role.Role;
import com.example.springsecurity6.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    public ResponseEntity<?> getAllUsersInformation() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    public ResponseEntity<?> assignRoleToUser(Integer id) {
        var user = userRepository.findById(id)
                .orElseThrow(()->new UsernameNotFoundException("User not found by id"));
        user.setRole(Role.ADMIN);
        return ResponseEntity.ok(userRepository.save(user));
    }

    public void deleteAccount(Integer id) {
        var user = userRepository.findById(id)
                .orElseThrow(()->new UsernameNotFoundException("User not found by id"));
        userRepository.deleteById(user.getId());
    }

}
