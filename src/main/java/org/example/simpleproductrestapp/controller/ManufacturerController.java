package org.example.simpleproductrestapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.simpleproductrestapp.dto.manufacturer.ManufacturerSaveDto;
import org.example.simpleproductrestapp.dto.manufacturer.ManufacturerUpdateDto;
import org.example.simpleproductrestapp.dto.product.ProductUpdateDto;
import org.example.simpleproductrestapp.entity.Manufacturer;
import org.example.simpleproductrestapp.entity.Product;
import org.example.simpleproductrestapp.service.ManufacturerServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manufacturer")
@RequiredArgsConstructor
public class ManufacturerController {
    private final ManufacturerServiceImpl manufacturerService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Manufacturer saveManufacturer(@Valid @RequestBody ManufacturerSaveDto manufacturerSaveDto){
        return manufacturerService.save(manufacturerSaveDto);
    }

    @GetMapping()
    public List<Manufacturer> findAll(){
        return manufacturerService.findAll();
    }

    @PutMapping("/{id}")
    public Manufacturer updateManufacturer(@PathVariable("id") Integer id, @Valid @RequestBody ManufacturerUpdateDto manufacturerUpdateDto){
        return manufacturerService.update(id, manufacturerUpdateDto);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") Integer id){
        return manufacturerService.delete(id);
    }

}
