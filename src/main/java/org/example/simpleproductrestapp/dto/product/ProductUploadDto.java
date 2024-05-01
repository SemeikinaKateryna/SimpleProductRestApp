package org.example.simpleproductrestapp.dto.product;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect
public class ProductUploadDto {
    private Integer id;
    private String name;
    private Integer releaseYear;
    private Double price;
    @JsonProperty("manufacturer_id")
    private Integer manufacturerId;
    private List<String> categories;
}
