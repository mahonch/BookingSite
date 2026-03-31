package com.forum.booking.service;

import com.forum.booking.entity.Admin;
import com.forum.booking.repository.AdminRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    public String authenticate(String username, String password) {
        Optional<Admin> adminOpt = adminRepository.findByUsername(username);
        
        if (adminOpt.isEmpty()) {
            throw new RuntimeException("Пользователь не найден");
        }

        Admin admin = adminOpt.get();
        
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }

        return generateToken(admin);
    }

    public Admin registerAdmin(String username, String rawPassword) {
        if (adminRepository.existsByUsername(username)) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        Admin admin = Admin.builder()
            .username(username)
            .password(passwordEncoder.encode(rawPassword))
            .build();

        return adminRepository.save(admin);
    }

    public Optional<Admin> getAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    private String generateToken(Admin admin) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
            .subject(admin.getUsername())
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key)
            .compact();
    }

    public String getUsernameFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }
}
