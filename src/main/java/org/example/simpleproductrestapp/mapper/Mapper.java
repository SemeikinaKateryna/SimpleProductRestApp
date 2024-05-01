package org.example.simpleproductrestapp.mapper;

import org.example.simpleproductrestapp.dto.ProductSaveDto;
import org.example.simpleproductrestapp.dto.ProductUpdateDto;
import org.example.simpleproductrestapp.dto.ProductUploadDto;
import org.example.simpleproductrestapp.entity.Product;

public class Mapper {
    public static void mapSaveDtoFromUpdateDto(Integer id, ProductSaveDto productSaveDto, ProductUpdateDto productDto) {
        productSaveDto.setId(id);
        productSaveDto.setName(productDto.getName());
        productSaveDto.setReleaseYear(productDto.getReleaseYear());
        productSaveDto.setPrice(productDto.getPrice());
        productSaveDto.setManufacturer_id(productDto.getManufacturer_id());
        productSaveDto.setCategories(productDto.getCategories());
    }
    public static void mapSaveDtoFromUpload(ProductSaveDto productSaveDto, ProductUploadDto productDto) {
        productSaveDto.setId(productDto.getId());
        productSaveDto.setName(productDto.getName());
        productSaveDto.setReleaseYear(productDto.getReleaseYear());
        productSaveDto.setPrice(productDto.getPrice());
        productSaveDto.setManufacturer_id(productDto.getManufacturerId());
        productSaveDto.setCategories(productDto.getCategories());
    }

}
