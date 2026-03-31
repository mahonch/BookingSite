package com.forum.booking.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlotDto {

    private String time;
    private Boolean isAvailable;
}
