package org.example.simpleproductrestapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductUpdateDto {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Year of release cannot be null")
    private Integer releaseYear;

    @NotNull(message = "Price cannot be null")
    private Double price;

    @NotNull(message = "Manufacturer cannot be null")
    private Integer manufacturer_id;

    @NotNull(message = "Categories cannot be null")
    private List<String> categories;
}
