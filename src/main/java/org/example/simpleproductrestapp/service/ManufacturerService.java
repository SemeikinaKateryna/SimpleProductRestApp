package org.example.simpleproductrestapp.service;
import org.example.simpleproductrestapp.dto.manufacturer.ManufacturerSaveDto;
import org.example.simpleproductrestapp.dto.manufacturer.ManufacturerUpdateDto;

import java.util.List;

public interface ManufacturerService {
    ManufacturerSaveDto save(ManufacturerSaveDto man);

    List<ManufacturerSaveDto> findAll();

    ManufacturerSaveDto update(Integer id, ManufacturerUpdateDto productUpdateDto);

    boolean delete(Integer id);
}
