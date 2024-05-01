package org.example.simpleproductrestapp.service;


import org.example.simpleproductrestapp.dto.manufacturer.ManufacturerSaveDto;
import org.example.simpleproductrestapp.dto.manufacturer.ManufacturerUpdateDto;
import org.example.simpleproductrestapp.entity.Manufacturer;

import java.util.List;

public interface ManufacturerService {
    Manufacturer save(ManufacturerSaveDto man);

    List<Manufacturer> findAll();

    Manufacturer update(Integer id, ManufacturerUpdateDto productUpdateDto);

    boolean delete(Integer id);
}
