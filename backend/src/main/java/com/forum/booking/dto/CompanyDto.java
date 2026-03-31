package com.forum.booking.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDto {

    private Long id;
    private String name;
    private String description;
    private String logoUrl;
    private Boolean isActive;
    private LocalDateTime createdAt;
    
    // Информация о бронировании компании
    private Boolean isBooked;
    private String bookedTime;
    private String bookedBy; // Имя фамилия кто забронировал
    
    private List<String> bookedSlots;
    private List<TimeSlotDto> availableSlots;
}
