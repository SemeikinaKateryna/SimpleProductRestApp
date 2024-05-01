package org.example.simpleproductrestapp.dto.manufacturer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ManufacturerUpdateDto {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Start cooperation date cannot be null")
    private LocalDate start_cooperation_date;

    @NotBlank(message = "Contact number cannot be blank")
    private String contact_number;

    @NotBlank(message = "Email cannot be blank")
    private String email;
}
