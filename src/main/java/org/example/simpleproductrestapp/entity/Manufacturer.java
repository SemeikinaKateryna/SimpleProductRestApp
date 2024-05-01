package org.example.simpleproductrestapp.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="manufacturer")
public class Manufacturer {
    @Id
    private Integer id;
    private String name;
    private LocalDate startCooperationDate;
    private String contactNumber;
    private String email;

}