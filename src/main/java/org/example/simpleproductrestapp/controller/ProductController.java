package org.example.simpleproductrestapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.simpleproductrestapp.dto.product.ProductSaveDto;
import org.example.simpleproductrestapp.dto.product.ProductUpdateDto;
import org.example.simpleproductrestapp.entity.Product;
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
    public Product saveProduct(@Valid @RequestBody ProductSaveDto productSaveDto){
        return productService.save(productSaveDto);
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable("id") Integer id){
        return productService.getById(id);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable("id") Integer id, @Valid @RequestBody ProductUpdateDto productUpdateDto){
        return productService.update(id, productUpdateDto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteProduct(@PathVariable("id") Integer id){
        return productService.delete(id);
    }

//    @PostMapping("/_list")
//    public List<ProductListDto> listOfProducts(@RequestBody ProductSpecifications productSpecifications){
//        return productService.list(productSpecifications);
//    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean upload(){
        return productService.upload();
    }

}
