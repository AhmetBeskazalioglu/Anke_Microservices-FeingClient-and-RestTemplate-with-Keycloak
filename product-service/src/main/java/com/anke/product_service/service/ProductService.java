package com.anke.product_service.service;

import com.anke.product_service.entity.Product;
import com.anke.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService {


    @Autowired
    ProductRepository productRepository;

    //database deki eklenen kayıt satırı nesneye dönüştürülüp bu metodu kim nerde
    //çağırıyorsa ona json olarak döner  {"name":"urun"}
    public Product createProduct(Product product) {
        //jdbc insert into product (name,price,description) values(product.name,product.price);
        return productRepository.save(product);
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found in DB"));
    }

    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok().body(productRepository.findAll());
    }

    public Product updateProduct(Long productId, Product product) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found in DB"));

        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setCategory(product.getCategory());

        return productRepository.save(existingProduct);
    }

    public String deleteProduct(Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found in DB"));

        if (existingProduct != null) {
            productRepository.delete(existingProduct);
            return "Product with id: " + productId + " deleted successfully";
        } else {
            return "Product with id: " + productId + " not found in DB";
        }
    }


}
