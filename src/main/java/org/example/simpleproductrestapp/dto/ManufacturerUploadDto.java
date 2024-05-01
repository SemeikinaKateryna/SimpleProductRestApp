package org.example.simpleproductrestapp.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect
public class ManufacturerUploadDto {
    String name;

    @JsonProperty("start_cooperation_date")
    LocalDate startCooperationDate;

    @JsonProperty("contact_number")
    String contactNumber;

    String email;
}
