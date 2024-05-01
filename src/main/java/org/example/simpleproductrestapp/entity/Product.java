package org.example.simpleproductrestapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="product")
public class Product {
    @Id
    private Integer id;

    private String name;

    private Integer releaseYear;

    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="manufacturer_id")
    private Manufacturer manufacturer;

    @ElementCollection
    private List<String> categories;

}