package org.example.simpleproductrestapp.validator;

import lombok.extern.slf4j.Slf4j;
import org.example.simpleproductrestapp.dto.manufacturer.ManufacturerSaveDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class ManufacturerValidator {
    public static ManufacturerSaveDto validateManufacturer(ManufacturerSaveDto manufacturerSaveDto) {
        if (manufacturerSaveDto.getStart_cooperation_date().isAfter(LocalDate.now())) {
            log.debug("Start cooperation date cannot be after now!");
            return null;
        }
        if ( manufacturerSaveDto.getContact_number().length() != 11) {
            log.debug("Contact number must be 11 digits!");
            return null;
        }
        if(!isValidEmail(manufacturerSaveDto.getEmail())){
            log.debug("Email must contains '@'!");
            return null;
        }
        return manufacturerSaveDto;
    }
    private static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
