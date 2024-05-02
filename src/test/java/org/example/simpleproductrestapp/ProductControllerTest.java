package org.example.simpleproductrestapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.simpleproductrestapp.dto.product.ProductSaveDto;
import org.example.simpleproductrestapp.entity.Manufacturer;
import org.example.simpleproductrestapp.entity.Product;
import org.example.simpleproductrestapp.repository.ManufacturerRepository;
import org.example.simpleproductrestapp.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SimpleProductRestAppApplication.class)
@AutoConfigureMockMvc
public class ProductControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        Manufacturer manufacturer = new Manufacturer(23, "T-shirt Hub", LocalDate.now(),"+1234567333", "info@hub.com");
        manufacturerRepository.save(manufacturer);
        Product product = new Product(25, "Just T-shirt", 2021, 109.99, manufacturer, List.of("Unisex", "Clothing"));
        productRepository.save(product);
    }

    @AfterEach
    public void afterEach() {
        productRepository.deleteAll();
    }

    @Test
    public void testSaveProduct() throws Exception{
        String body = """
          {
            "id": 26,
            "name": "Casual T-shirt",
            "releaseYear": 2022,
            "price": 252.00,
             "manufacturer_id": 23,
             "categories": ["Unisex", "Clothing"]
           }             
        """;
        MvcResult mvcResult = mvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isCreated())
                .andReturn();

        ProductSaveDto response = parseResponse(mvcResult, ProductSaveDto.class);

        int productId = response.getId();
        assertThat(productId).isGreaterThanOrEqualTo(1);

        Product product = productRepository.findByIdWithManufacturer(productId).orElse(null);
        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(26);
        assertThat(product.getName()).isEqualTo("Casual T-shirt");
        assertThat(product.getReleaseYear()).isEqualTo(2022);
        assertThat(product.getPrice()).isEqualTo(252.00);
        assertThat(product.getManufacturer().getId()).isEqualTo(23);
    }
    @Test
    public void testUpdateProduct() throws Exception {

        String updatedBody = """
                  {
                    "name": "Updated T-shirt",
                    "releaseYear": 2023,
                    "price": 299.99,
                     "manufacturer_id": 23,
                     "categories": ["Unisex", "Clothing"]
                   }             
                """;

        MvcResult mvcResult = mvc.perform(put("/api/product/25")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        ProductSaveDto response = parseResponse(mvcResult, ProductSaveDto.class);

        assertThat(response.getName()).isEqualTo("Updated T-shirt");
        assertThat(response.getReleaseYear()).isEqualTo(2023);
        assertThat(response.getPrice()).isEqualTo(299.99);
        assertThat(response.getManufacturer_id()).isEqualTo(23);
    }

    @Test
    public void testDeleteProduct() throws Exception {
        mvc.perform(delete("/api/product/25"))
                .andExpect(status().isOk());

        assertThat(productRepository.findById(25L)).isEmpty();
    }

    @Test
    public void testGetById() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/api/product/25"))
                .andExpect(status().isOk())
                .andReturn();

        ProductSaveDto response = parseResponse(mvcResult, ProductSaveDto.class);

        assertThat(response.getId()).isEqualTo(25);
    }

    @Test
    public void testGenerateReport() throws Exception {
        String reportRequestJson = "{\"manufacturerId\": 4, \"releaseYear\": 2023}";

        MvcResult mvcResult = mvc.perform(post("/api/product/_report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reportRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.ms-excel"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=filtered_products_report.xlsx"))
                .andReturn();

        byte[] contentBytes = mvcResult.getResponse().getContentAsByteArray();
        try (InputStream inputStream = new ByteArrayInputStream(contentBytes)) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            Row headerRow = sheet.getRow(0);
            assertThat(headerRow.getCell(0).getStringCellValue()).isEqualTo("ID");
            assertThat(headerRow.getCell(1).getStringCellValue()).isEqualTo("Product");
            assertThat(headerRow.getCell(2).getStringCellValue()).isEqualTo("Release Year");
            assertThat(headerRow.getCell(3).getStringCellValue()).isEqualTo("Price");
            assertThat(headerRow.getCell(4).getStringCellValue()).isEqualTo("Manufacturer ID");
            assertThat(headerRow.getCell(5).getStringCellValue()).isEqualTo("Manufacturer Name");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row dataRow = sheet.getRow(i);
                assertThat(dataRow.getCell(2)).isEqualTo(2023);
                assertThat(dataRow.getCell(4)).isEqualTo(4);
            }
        }
    }
    @Test
    public void testUploadProducts() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/api/product/upload"))
                .andExpect(status().isCreated())
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(201);
    }
    private <T>T parseResponse(MvcResult mvcResult, Class<T> c) {
        try {
            return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), c);
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Error parsing json", e);

        }
    }
}
