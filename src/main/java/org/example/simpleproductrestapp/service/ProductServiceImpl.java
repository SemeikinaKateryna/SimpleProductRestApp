package org.example.simpleproductrestapp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.simpleproductrestapp.dto.product.ProductSaveDto;
import org.example.simpleproductrestapp.dto.product.ProductUpdateDto;
import org.example.simpleproductrestapp.dto.product.ProductUploadDto;
import org.example.simpleproductrestapp.entity.Manufacturer;
import org.example.simpleproductrestapp.entity.Product;
import org.example.simpleproductrestapp.filters.ProductRequests;
import org.example.simpleproductrestapp.mapper.Mapper;
import org.example.simpleproductrestapp.repository.ManufacturerRepository;
import org.example.simpleproductrestapp.repository.ProductRepository;
import org.example.simpleproductrestapp.validator.ProductValidator;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ManufacturerRepository manufacturerRepository;

    private final ObjectMapper objectMapper;

    @Override
    public ProductSaveDto save(ProductSaveDto productDto) {
        if (ProductValidator.validateProduct(productDto) == null) {
            return null;
        }
        productRepository.save(getDataFromSaveDto(productDto));
        return productDto;
    }

    @Override
    @Transactional
    public ProductSaveDto getById(Integer id) {
        Optional<Product> productOptional = productRepository.findByIdWithManufacturer(id.longValue());
        if(productOptional.isEmpty()){
            return null;
        }
        ProductSaveDto productSaveDto = new ProductSaveDto();
        Mapper.ProductMapping.mapSaveDtoFromData(productOptional.get(),productSaveDto);
        return productSaveDto;
    }

    @Override
    public ProductSaveDto update(Integer id, ProductUpdateDto productUpdateDto) {
        Optional<Product> productOptional = productRepository.findByIdWithManufacturer(id);
        if (productOptional.isEmpty()) {
            log.debug(String.format("Product with id %d not found!", id));
            return null;
        }

        ProductSaveDto productSaveDto = new ProductSaveDto();
        Mapper.ProductMapping.mapSaveDtoFromUpdateDto(id, productSaveDto, productUpdateDto);
        return save(productSaveDto);
    }


    @Override
    public boolean delete(Integer id) {
        Optional<Product> product = productRepository.findByIdWithManufacturer(id);
        if (product.isEmpty()) {
            log.debug("Product with id %d not found!".formatted(id));
            return false;
        } else {
            productRepository.deleteById(id.longValue());
            return true;
        }
    }
    @Override
    public void report(HttpServletResponse response, ProductRequests reportRequest) {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=filtered_products_report.xlsx");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Report");

            // Создание строки с названиями столбцов
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Product");
            headerRow.createCell(2).setCellValue("Release Year");
            headerRow.createCell(3).setCellValue("Price");
            headerRow.createCell(4).setCellValue("Manufacturer ID");
            headerRow.createCell(5).setCellValue("Manufacturer Name");

            int rowNum = 1;
            List<Product> filtered = productRepository.findAllByManufacturerIdAndReleaseYear(reportRequest.getManufacturerId(), reportRequest.getReleaseYear());
            for (Product entity : filtered) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entity.getId());
                row.createCell(1).setCellValue(entity.getName());
                row.createCell(2).setCellValue(entity.getReleaseYear());
                row.createCell(3).setCellValue(entity.getPrice());
                row.createCell(4).setCellValue(entity.getManufacturer().getId());
                row.createCell(5).setCellValue(entity.getManufacturer().getName());
            }

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                response.getOutputStream().write(outputStream.toByteArray());
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean upload() {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.findAndRegisterModules();

        Map<String, Integer> uploadStatistics = uploadFromFile(new File("./jsonFiles/products.json"));

        return writeToFile(uploadStatistics);
    }

    private Map<String, Integer> uploadFromFile(File file) {
        int successCount = 0;
        int failureCount = 0;

        try {
            List<ProductUploadDto> productUploadDtos = objectMapper.readValue(file, new TypeReference<>() {
            });
            for (ProductUploadDto uploadDto : productUploadDtos) {
                ProductSaveDto productSaveDto = new ProductSaveDto();
                Mapper.ProductMapping.mapSaveDtoFromUpload(productSaveDto, uploadDto);
                if (save(productSaveDto) != null) {
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

    private boolean writeToFile(Map<String, Integer> map) {
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


}
