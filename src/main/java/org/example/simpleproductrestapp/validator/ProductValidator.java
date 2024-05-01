package org.example.simpleproductrestapp.validator;

import lombok.extern.slf4j.Slf4j;
import org.example.simpleproductrestapp.dto.product.ProductSaveDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProductValidator {
    public static ProductSaveDto validateProduct(ProductSaveDto productDto) {
        if (productDto.getReleaseYear() != null && productDto.getReleaseYear() > (LocalDate.now().getYear())) {
            log.debug("Release year cannot be more than current year!");
            return null;
        }
        if(productDto.getPrice() <= 0){
            log.debug("Price cannot be 0 or less!");
            return null;
        }
        if(productDto.getCategories().stream().allMatch(String::isBlank)){
            log.debug("All categories in the list cannot be blank!");
            return null;
        }else if(productDto.getCategories().stream().anyMatch(String::isBlank)){
            productDto.setCategories(productDto.getCategories()
                    .stream()
                    .filter(category -> !category.isBlank())
                    .collect(Collectors.toList()));
            log.debug("Some blank categories was removed from list!");
        }

        return productDto;
    }
}
