package com.fatih.takasapp.controller;

import com.fatih.takasapp.dto.AuthResponse;
import com.fatih.takasapp.dto.LoginRequest;
import com.fatih.takasapp.entity.User;
import com.fatih.takasapp.repository.UserRepository;
import com.fatih.takasapp.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authenticationService;

    // Kullanıcı kayıt
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email zaten kayıtlı!");
        }
        if (user.getFirstName() == null || user.getLastName() == null) {
            return ResponseEntity.badRequest().body("Ad ve soyad zorunludur!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Kayıt başarılı!");
    }

    // Kullanıcı login ve JWT dön
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email adresi boş olamaz"));
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Şifre boş olamaz"));
        }
        try {
            AuthResponse response = authenticationService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("message", "Geçersiz email veya şifre!"));
        }
    }

    // Tüm kullanıcıları getir
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Kullanıcı silme
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("Kullanıcı başarıyla silindi!");
        }
        return ResponseEntity.notFound().build();
    }
}
