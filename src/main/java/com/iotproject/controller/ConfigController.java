package com.iotproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

@RestController
@RequestMapping("/api/mail")
public class ConfigController {

    @GetMapping
    public ResponseEntity<?> getEmail() {
        try {
            String email = Files.readString(Paths.get("config/email.txt")).trim();
            return ResponseEntity.ok(Map.of("email", email));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Could not read email: " + e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<?> setEmail(@RequestBody String newEmail) {
        String emailTrimmed = newEmail.trim();
        if (emailTrimmed.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email must not be empty"));
        }
        try {
            Files.writeString(Paths.get("config/email.txt"), emailTrimmed, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return ResponseEntity.ok(Map.of("message", "Email updated successfully", "email", emailTrimmed));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to update email: " + e.getMessage()));
        }
    }
} 