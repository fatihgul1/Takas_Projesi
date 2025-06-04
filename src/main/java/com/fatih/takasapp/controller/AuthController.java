package com.fatih.takasapp.controller;

import com.fatih.takasapp.dto.AuthResponse;
import com.fatih.takasapp.dto.LoginRequest;
import com.fatih.takasapp.entity.User;
import com.fatih.takasapp.repository.UserRepository;
import com.fatih.takasapp.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email zaten kayıtlı!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getFirstName() == null || user.getLastName() == null) {
            return ResponseEntity.badRequest().body("Ad ve soyad zorunludur!");
        }
        userRepository.save(user);
        return ResponseEntity.ok("Kayıt başarılı!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Login attempt for email: {}", loginRequest.getEmail());

        try {
            if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
                logger.warn("Login attempt with empty email");
                return ResponseEntity.badRequest().body(Map.of("message", "Email adresi boş olamaz"));
            }
            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                logger.warn("Login attempt with empty password");
                return ResponseEntity.badRequest().body(Map.of("message", "Şifre boş olamaz"));
            }

            AuthResponse response = authenticationService.login(loginRequest);

            // Burada detaylı response'u logla:
            logger.info("Login successful response: token={}, userId={}", response.getToken(), response.getUser().getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Login failed for user: {}. Error: {}", loginRequest.getEmail(), e.getMessage());
            return ResponseEntity.status(401).body(Map.of("message", "Geçersiz email veya şifre!"));
        }
    }


    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("Kullanıcı başarıyla silindi!");
        }
        return ResponseEntity.notFound().build();
    }
}
