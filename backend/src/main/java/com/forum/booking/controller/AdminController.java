package com.forum.booking.controller;

import com.forum.booking.dto.*;
import com.forum.booking.service.AuthService;
import com.forum.booking.service.BookingService;
import com.forum.booking.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class AdminController {

    private final AuthService authService;
    private final CompanyService companyService;
    private final BookingService bookingService;

    @PostMapping("/auth/login")
    public ResponseEntity<JwtAuthResponseDto> login(@RequestBody AdminLoginDto loginDto) {
        String token = authService.authenticate(loginDto.getUsername(), loginDto.getPassword());
        
        JwtAuthResponseDto response = JwtAuthResponseDto.builder()
            .token(token)
            .username(loginDto.getUsername())
            .expiresIn(86400000L)
            .build();
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<JwtAuthResponseDto> register(@RequestBody AdminLoginDto registerDto) {
        authService.registerAdmin(registerDto.getUsername(), registerDto.getPassword());
        
        String token = authService.authenticate(registerDto.getUsername(), registerDto.getPassword());
        
        JwtAuthResponseDto response = JwtAuthResponseDto.builder()
            .token(token)
            .username(registerDto.getUsername())
            .expiresIn(86400000L)
            .build();
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/companies")
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<CompanyDto> getCompany(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @PostMapping("/companies")
    public ResponseEntity<CompanyDto> createCompany(@Valid @RequestBody CompanyCreateDto dto) {
        CompanyDto created = companyService.createCompany(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/companies/{id}")
    public ResponseEntity<CompanyDto> updateCompany(
            @PathVariable Long id,
            @Valid @RequestBody CompanyUpdateDto dto) {
        CompanyDto updated = companyService.updateCompany(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/bookings/company/{companyId}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByCompany(
            @PathVariable Long companyId) {
        return ResponseEntity.ok(bookingService.getBookingsByCompany(companyId));
    }
}
