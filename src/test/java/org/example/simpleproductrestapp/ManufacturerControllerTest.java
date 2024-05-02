package org.example.simpleproductrestapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.simpleproductrestapp.dto.manufacturer.ManufacturerSaveDto;
import org.example.simpleproductrestapp.entity.Manufacturer;
import org.example.simpleproductrestapp.repository.ManufacturerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SimpleProductRestAppApplication.class)
@AutoConfigureMockMvc
public class ManufacturerControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        Manufacturer manufacturer = new Manufacturer(24, "Just T-shirt", LocalDate.now(), "+1234222333", "info23@hub.com");
        manufacturerRepository.save(manufacturer);
    }

    @AfterEach
    public void afterEach() {
        manufacturerRepository.deleteAll();
    }

    @Test
    public void testSaveManufacturer() throws Exception{
        String body = """
          {
              "id": 25,
              "name": "Good Cloth",
              "start_cooperation_date": "2024-05-02",
              "contact_number": "+1234521333",
              "email": "infoGC@hub.com"
           }   
        """;
        MvcResult mvcResult = mvc.perform(post("/api/manufacturer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isCreated())
                .andReturn();

        ManufacturerSaveDto response = parseResponse(mvcResult, ManufacturerSaveDto.class);

        int manufacturerId = response.getId();
        assertThat(manufacturerId).isGreaterThanOrEqualTo(1);

        Manufacturer manufacturer = manufacturerRepository.findById((long) manufacturerId).orElse(null);
        assertThat(manufacturer).isNotNull();
        assertThat(manufacturer.getId()).isEqualTo(25);
        assertThat(manufacturer.getName()).isEqualTo("Good Cloth");
        assertThat(manufacturer.getStartCooperationDate()).isEqualTo("2024-05-02");
        assertThat(manufacturer.getContactNumber()).isEqualTo("+1234521333");
        assertThat(manufacturer.getEmail()).isEqualTo("infoGC@hub.com");
    }
    @Test
    public void testUpdateManufacturer() throws Exception {
        String updatedBody = """
                  {
                    "name": "Updated Good Cloth",
                    "start_cooperation_date": "2013-05-02",
                    "contact_number": "+1234521330",
                    "email": "infoG1C@hub.com"
                   }             
                """;

        MvcResult mvcResult = mvc.perform(put("/api/manufacturer/24")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        ManufacturerSaveDto response = parseResponse(mvcResult, ManufacturerSaveDto.class);

        assertThat(response.getName()).isEqualTo("Updated Good Cloth");
        assertThat(response.getStart_cooperation_date()).isEqualTo("2013-05-02");
        assertThat(response.getContact_number()).isEqualTo("+1234521330");
        assertThat(response.getEmail()).isEqualTo("infoG1C@hub.com");

    }

    @Test
    public void testDeleteProduct() throws Exception {
        mvc.perform(delete("/api/manufacturer/24"))
                .andExpect(status().isOk());

        assertThat(manufacturerRepository.findById(24L)).isEmpty();
    }

    @Test
    public void testGetAll() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/api/manufacturer"))
                .andExpect(status().isOk())
                .andReturn();
        List<ManufacturerSaveDto> response = parseResponseList(mvcResult, new TypeReference<>() {});

        assertThat(response).isNotNull();
        assertThat(response.size()).isGreaterThanOrEqualTo(1);
    }
    private <T> T parseResponseList(MvcResult mvcResult, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), typeReference);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing json", e);
        }
    }

    private <T>T parseResponse(MvcResult mvcResult, Class<T> c) {
        try {
            return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), c);
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Error parsing json", e);

        }
    }
}
