package org.example.simpleproductrestapp.service;

import jakarta.servlet.http.HttpServletResponse;
import org.example.simpleproductrestapp.dto.product.ProductSaveDto;
import org.example.simpleproductrestapp.dto.product.ProductUpdateDto;
import org.example.simpleproductrestapp.filters.ProductRequests;

public interface ProductService {
    ProductSaveDto save(ProductSaveDto pr);

    ProductSaveDto getById(Integer id);

    ProductSaveDto update(Integer id, ProductUpdateDto productUpdateDto);

    boolean delete(Integer id);

    boolean upload();

    void report(HttpServletResponse response, ProductRequests reportRequest);
}
