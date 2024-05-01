package org.example.simpleproductrestapp.service;

import org.example.simpleproductrestapp.dto.product.ProductSaveDto;
import org.example.simpleproductrestapp.dto.product.ProductUpdateDto;
import org.example.simpleproductrestapp.entity.Product;

public interface ProductService {
    Product save(ProductSaveDto pr);

    Product getById(Integer id);

    Product update(Integer id, ProductUpdateDto productUpdateDto);

    boolean delete(Integer id);
    //List<ProductListDto> list(ProductSpecifications productSpecifications);

    boolean upload();
}
