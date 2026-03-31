package com.forum.booking.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyCreateDto {

    @NotBlank(message = "Название компании обязательно")
    @Size(min = 1, max = 255, message = "Название должно быть от 1 до 255 символов")
    private String name;

    @Size(max = 1000, message = "Описание не более 1000 символов")
    private String description;

    private String logoUrl;
}
