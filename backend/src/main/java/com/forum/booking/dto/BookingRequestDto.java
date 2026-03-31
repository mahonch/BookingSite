package com.forum.booking.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequestDto {

    @NotNull(message = "ID компании обязателен")
    private Long companyId;

    @NotBlank(message = "Имя обязательно")
    @Size(min = 1, max = 100, message = "Имя должно быть от 1 до 100 символов")
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    @Size(min = 1, max = 100, message = "Фамилия должна быть от 1 до 100 символов")
    private String lastName;

    @NotBlank(message = "Телефон обязателен")
    @Pattern(regexp = "^\\+?[0-9\\s\\-()]{7,20}$", message = "Неверный формат телефона")
    private String phone;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Неверный формат email")
    private String email;

    @NotNull(message = "Дата бронирования обязательна")
    private java.time.LocalDate bookingDate;

    @NotBlank(message = "Временной слот обязателен")
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Неверный формат времени")
    private String timeSlot;
}
