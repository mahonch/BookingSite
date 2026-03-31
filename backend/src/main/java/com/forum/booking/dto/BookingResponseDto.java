package com.forum.booking.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDto {

    private Long id;
    private Long companyId;
    private String companyName;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private LocalDate bookingDate;
    private String timeSlot;
    private LocalDateTime createdAt;
}
