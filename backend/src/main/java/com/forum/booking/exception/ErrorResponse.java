package com.forum.booking.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private long timestamp;

    public static ErrorResponse of(HttpStatus status, String message) {
        return new ErrorResponse(status.value(), message, System.currentTimeMillis());
    }
}
