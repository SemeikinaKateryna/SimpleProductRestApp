package org.example.simpleproductrestapp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.simpleproductrestapp.dto.ProductSaveDto;
import org.example.simpleproductrestapp.dto.ProductUpdateDto;
import org.example.simpleproductrestapp.dto.ProductUploadDto;
import org.example.simpleproductrestapp.entity.Manufacturer;
import org.example.simpleproductrestapp.entity.Product;
import org.example.simpleproductrestapp.mapper.Mapper;
import org.example.simpleproductrestapp.repository.ManufacturerRepository;
import org.example.simpleproductrestapp.repository.ProductRepository;
import org.example.simpleproductrestapp.validator.ProductValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ManufacturerRepository manufacturerRepository;

    private final ObjectMapper objectMapper;

    @Override
    public Product save(ProductSaveDto productDto) {
        if (ProductValidator.validateProduct(productDto) == null) {
            return null;
        }

        Product data = getDataFromSaveDto(productDto);
        if (data == null) {
            return null;
        }

        return productRepository.save(data);
    }


    @Override
    @Transactional
    public Product getById(Integer id) {
        return productRepository.findByIdWithManufacturer(id.longValue()).orElse(null);
    }

    @Override
    public Product update(Integer id, ProductUpdateDto productUpdateDto) {
        Optional<Product> productOptional = productRepository.findByIdWithManufacturer(id);
        if (productOptional.isEmpty()) {
            log.debug(String.format("Product with id %d not found!", id));
            return null;
        }

        Product product = productOptional.get();
        if (!reasonInUpdate(product, productUpdateDto)) {
            log.debug("No changes happened, because same object is in db!");
            return product;
        }

        ProductSaveDto productSaveDto = new ProductSaveDto();
        Mapper.mapSaveDtoFromUpdateDto(id, productSaveDto, productUpdateDto);
        return save(productSaveDto);
    }


    @Override
    public void delete(Integer id) {
        Optional<Product> product = productRepository.findByIdWithManufacturer(id);
        if (product.isEmpty()) {
            log.debug("Product with id %d not found!".formatted(id));
        } else {
            productRepository.delete(product.get());
        }
    }

    @Override
    public boolean upload() {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.findAndRegisterModules();

        Map<String, Integer> uploadStatistics = uploadFromFile(new File("./jsonFiles/products.json"));

        return writeToFile(uploadStatistics);
    }
    
    private Map<String, Integer> uploadFromFile(File file){
        int successCount = 0;
        int failureCount = 0;

        try {
            List<ProductUploadDto> productUploadDtos = objectMapper.readValue(file, new TypeReference<List<ProductUploadDto>>() {});
            for (ProductUploadDto uploadDto : productUploadDtos) {
                ProductSaveDto productSaveDto = new ProductSaveDto();
                Mapper.mapSaveDtoFromUpload(productSaveDto,uploadDto);
                if(save(productSaveDto)!= null){
                    successCount++;
                } else {
                    failureCount++;
                }
            }
            return Map.of("successUploads", successCount, "failureUploads", failureCount);
        } catch (IOException e) {
            log.debug("Error reading file: " + file.getAbsolutePath());
            return null;
        }
    }

    private boolean writeToFile(Map<String, Integer> map){
        try {
            objectMapper.writeValue(new File("./jsonFiles/uploadStatistics.json"), map);
            return true;
        } catch (Exception e) {
            log.debug("Error writing file: ");
            return false;
        }
    }
    
    private Product getDataFromSaveDto(ProductSaveDto productDto) {
        Product data = null;
        Optional<Manufacturer> manufacturer = resolveManufacturer(productDto.getManufacturer_id());
        if (manufacturer.isPresent()) {
            data = new Product();
            data.setId(productDto.getId());
            data.setName(productDto.getName());
            data.setReleaseYear(productDto.getReleaseYear());
            data.setPrice(productDto.getPrice());
            data.setManufacturer(manufacturer.get());
            data.setCategories(productDto.getCategories());
        }
        return data;
    }
    private Optional<Manufacturer> resolveManufacturer(Integer manufacturerId) {
        if (manufacturerId == null) {
            return Optional.empty();
        }
        Optional<Manufacturer> manufacturerById = manufacturerRepository.findById(Long.valueOf(manufacturerId));
        if (manufacturerById.isEmpty()) {
            log.debug("Manufacturer with id %d not found!".formatted(manufacturerId));
            return Optional.empty();
        }
        return manufacturerById;
    }
    private boolean reasonInUpdate(Product productInDb, ProductUpdateDto productUpdateDto){
        return Objects.equals(productInDb.getName(), productUpdateDto.getName())
                && Objects.equals(productInDb.getReleaseYear(), productUpdateDto.getReleaseYear())
                && Objects.equals(productInDb.getPrice(), productUpdateDto.getPrice())
                && Objects.equals(productInDb.getManufacturer().getId(), productUpdateDto.getManufacturer_id())
                && Objects.equals(productInDb.getCategories(), productUpdateDto.getCategories());
    }




}
