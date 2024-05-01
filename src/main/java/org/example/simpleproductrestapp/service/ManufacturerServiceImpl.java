package org.example.simpleproductrestapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.simpleproductrestapp.dto.manufacturer.ManufacturerSaveDto;
import org.example.simpleproductrestapp.dto.manufacturer.ManufacturerUpdateDto;
import org.example.simpleproductrestapp.dto.product.ProductSaveDto;
import org.example.simpleproductrestapp.entity.Manufacturer;
import org.example.simpleproductrestapp.entity.Product;
import org.example.simpleproductrestapp.mapper.Mapper;
import org.example.simpleproductrestapp.repository.ManufacturerRepository;
import org.springframework.stereotype.Service;
import org.example.simpleproductrestapp.validator.ManufacturerValidator;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManufacturerServiceImpl implements ManufacturerService{
    private final ManufacturerRepository manufacturerRepository;

    @Override
    public Manufacturer save(ManufacturerSaveDto manSaveDto) {
        if (ManufacturerValidator.validateManufacturer(manSaveDto) == null) {
            return null;
        }

        Manufacturer data = getDataFromSaveDto(manSaveDto);
        return manufacturerRepository.save(data);
    }

    private Manufacturer getDataFromSaveDto(ManufacturerSaveDto manufacturerSaveDto) {
        Manufacturer data = new Manufacturer();
        data.setId(manufacturerSaveDto.getId());
        data.setName(manufacturerSaveDto.getName());
        data.setStartCooperationDate(manufacturerSaveDto.getStart_cooperation_date());
        data.setContactNumber(manufacturerSaveDto.getContact_number());
        data.setEmail(manufacturerSaveDto.getEmail());
        return data;
    }
    @Override
    public List<Manufacturer> findAll() {
        return manufacturerRepository.findAll();
    }

    @Override
    public Manufacturer update(Integer id, ManufacturerUpdateDto manufacturerUpdateDto) {
        Optional<Manufacturer> manufacturerOptional = manufacturerRepository.findById(Long.valueOf(id));
        if (manufacturerOptional.isEmpty()) {
            log.debug(String.format("Manufacturer with id %d not found!", id));
            return null;
        }

        ManufacturerSaveDto manufacturerSaveDto = new ManufacturerSaveDto();
        Mapper.ManufacturerMapping.mapSaveDtoFromUpdateDto(id, manufacturerSaveDto, manufacturerUpdateDto);
        return save(manufacturerSaveDto);
    }

    @Override
    public boolean delete(Integer id) {
        Optional<Manufacturer> manufacturer = manufacturerRepository.findById(Long.valueOf(id));
        if (manufacturer.isEmpty()) {
            log.debug("Manufacturer with id %d not found!".formatted(id));
            return false;
        } else {
            manufacturerRepository.deleteById(Long.valueOf(id));
            return true;
        }
    }
}
