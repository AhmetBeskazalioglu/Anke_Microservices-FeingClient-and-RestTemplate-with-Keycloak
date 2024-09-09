package com.anke.product_service.controller;

import com.anke.product_service.entity.Product;
import com.anke.product_service.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product")
@Tag(name = "product", description = "Product Endpoints")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping//{"name":"urun1","price":20.0,"description":"urun1 acik":"category":"ayakkabi"}
    public ResponseEntity<Product> yeniUrunEkle(@RequestBody Product product) {
        //jdbc insert into product
        return ResponseEntity.ok().body(productService.createProduct(product));
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> urunGetir(@PathVariable("id") Long productId) {
        return ResponseEntity.ok().body(productService.getProductById(productId));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return productService.getAll();
    }

    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long productId, @RequestBody Product product) {
        return ResponseEntity.ok().body(productService.updateProduct(productId, product));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long productId) {
        return ResponseEntity.ok().body(productService.deleteProduct(productId));
    }

}
