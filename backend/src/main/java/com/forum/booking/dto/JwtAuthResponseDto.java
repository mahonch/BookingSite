package com.forum.booking.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtAuthResponseDto {

    private String token;
    private String username;
    private Long expiresIn;
}
