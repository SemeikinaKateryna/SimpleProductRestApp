package org.example.simpleproductrestapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name="manufacturer")
@RequiredArgsConstructor
@AllArgsConstructor
public class Manufacturer {
    @Id
    private Integer id;
    private String name;
    private LocalDate startCooperationDate;
    private String contactNumber;
    private String email;

}