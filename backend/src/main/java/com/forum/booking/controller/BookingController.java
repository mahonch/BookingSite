package com.forum.booking.controller;

import com.forum.booking.dto.BookingRequestDto;
import com.forum.booking.dto.BookingResponseDto;
import com.forum.booking.dto.CompanyDto;
import com.forum.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/companies")
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        List<CompanyDto> companies = bookingService.getAllCompaniesWithSlots();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/companies/{companyId}")
    public ResponseEntity<CompanyDto> getCompany(@PathVariable Long companyId) {
        CompanyDto company = bookingService.getCompanyWithSlots(companyId);
        return ResponseEntity.ok(company);
    }

    @GetMapping("/forum-date")
    public ResponseEntity<Map<String, String>> getForumDate() {
        Map<String, String> response = new HashMap<>();
        response.put("date", bookingService.getForumDate().toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/bookings")
    public ResponseEntity<BookingResponseDto> createBooking(
            @Valid @RequestBody BookingRequestDto request) {
        BookingResponseDto booking = bookingService.createBooking(request);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/bookings/company/{companyId}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByCompany(
            @PathVariable Long companyId) {
        List<BookingResponseDto> bookings = bookingService.getBookingsByCompany(companyId);
        return ResponseEntity.ok(bookings);
    }
}
