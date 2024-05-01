package org.example.simpleproductrestapp.repository;

import org.example.simpleproductrestapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT prod FROM Product prod JOIN FETCH prod.manufacturer m WHERE prod.id = ?1")
    Optional<Product> findByIdWithManufacturer(@Param("prodId") long prodId);

}
