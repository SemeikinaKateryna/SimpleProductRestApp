package org.example.simpleproductrestapp.mapper;

import org.example.simpleproductrestapp.dto.manufacturer.ManufacturerSaveDto;
import org.example.simpleproductrestapp.dto.manufacturer.ManufacturerUpdateDto;
import org.example.simpleproductrestapp.dto.product.ProductSaveDto;
import org.example.simpleproductrestapp.dto.product.ProductUpdateDto;
import org.example.simpleproductrestapp.dto.product.ProductUploadDto;
import org.example.simpleproductrestapp.entity.Manufacturer;
import org.example.simpleproductrestapp.entity.Product;

public class Mapper {
    public static class ProductMapping {
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

        public static void mapSaveDtoFromData(Product saved, ProductSaveDto productSaveDto) {
            productSaveDto.setId(saved.getId());
            productSaveDto.setName(saved.getName());
            productSaveDto.setReleaseYear(saved.getReleaseYear());
            productSaveDto.setPrice(saved.getPrice());
            productSaveDto.setManufacturer_id(saved.getManufacturer().getId());
            productSaveDto.setCategories(saved.getCategories());
        }
    }
    public static class ManufacturerMapping{
        public static void mapSaveDtoFromUpdateDto(Integer id, ManufacturerSaveDto manufacturerSaveDto, ManufacturerUpdateDto manufacturerUpdateDto) {
            manufacturerSaveDto.setId(id);
            manufacturerSaveDto.setName(manufacturerUpdateDto.getName());
            manufacturerSaveDto.setStart_cooperation_date(manufacturerUpdateDto.getStart_cooperation_date());
            manufacturerSaveDto.setContact_number(manufacturerUpdateDto.getContact_number());
            manufacturerSaveDto.setEmail(manufacturerUpdateDto.getEmail());
        }
        public static ManufacturerSaveDto mapSaveDtoFromData(Manufacturer saved) {
            ManufacturerSaveDto manufacturerSaveDto = new ManufacturerSaveDto();
            manufacturerSaveDto.setId(saved.getId());
            manufacturerSaveDto.setName(saved.getName());
            manufacturerSaveDto.setStart_cooperation_date(saved.getStartCooperationDate());
            manufacturerSaveDto.setContact_number(saved.getContactNumber());
            manufacturerSaveDto.setEmail(saved.getEmail());
            return manufacturerSaveDto;
        }
    }

}
