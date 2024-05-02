package org.example.simpleproductrestapp.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.simpleproductrestapp.dto.product.ProductSaveDto;
import org.example.simpleproductrestapp.dto.product.ProductUpdateDto;
import org.example.simpleproductrestapp.filters.ProductRequests;
import org.example.simpleproductrestapp.service.ProductServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductServiceImpl productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductSaveDto saveProduct(@Valid @RequestBody ProductSaveDto productSaveDto){
        return productService.save(productSaveDto);
    }

    @GetMapping("/{id}")
    public ProductSaveDto getById(@PathVariable("id") Integer id){
        return productService.getById(id);
    }

    @PutMapping("/{id}")
    public ProductSaveDto updateProduct(@PathVariable("id") Integer id, @Valid @RequestBody ProductUpdateDto productUpdateDto){
        return productService.update(id, productUpdateDto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteProduct(@PathVariable("id") Integer id){
        return productService.delete(id);
    }

    @PostMapping(value = "/_report", produces = "application/vnd.ms-excel")
    public void generateReport(HttpServletResponse response, @RequestBody ProductRequests reportRequest) {
        productService.report(response, reportRequest);
    }
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean uploadProducts(){
        return productService.upload();
    }

}
