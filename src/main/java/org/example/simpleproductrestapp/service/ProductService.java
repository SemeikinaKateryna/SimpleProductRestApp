package org.example.simpleproductrestapp.service;

import org.example.simpleproductrestapp.dto.ProductSaveDto;
import org.example.simpleproductrestapp.dto.ProductUpdateDto;
import org.example.simpleproductrestapp.entity.Product;

public interface ProductService {
    Product save(ProductSaveDto pr);

    Product getById(Integer id);

    Product update(Integer id, ProductUpdateDto productUpdateDto);

    void delete(Integer id);

    boolean upload();
}
