package com.anke.shopping_cart_service.service;

import com.anke.shopping_cart_service.entity.Product;
import com.anke.shopping_cart_service.entity.ShoppingCart;
import com.anke.shopping_cart_service.repository.ProductRepository;
import com.anke.shopping_cart_service.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ShoppingCartService {

    @Autowired
    ShoppingCartRepository shoppingCartRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<ShoppingCart> create(String name) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setShoppincartName(name);
        return ResponseEntity.ok().body(shoppingCartRepository.save(shoppingCart));
    }

    public ResponseEntity<ShoppingCart> addProducts(Long shoppingCartId, List<Product> products) {
        ShoppingCart shoppingCart =
                shoppingCartRepository
                        .findById(shoppingCartId)
                        .orElseThrow(() -> new RuntimeException("Verilen id ile eşleşen bir sonuç bulunamadı"));

        products.forEach(product -> productRepository.saveAndFlush(product));

        Set<Product> newProducts = new HashSet<>(products);
        shoppingCart.setProducts(newProducts);
        return ResponseEntity.ok().body(shoppingCartRepository.save(shoppingCart));
    }

    public ResponseEntity<Map<String, String>> getShoppingCartPrice(Long shoppingCartId) {
        Map<String, String> response = new HashMap<>();

        ShoppingCart shoppingCart = shoppingCartRepository.findById(shoppingCartId)
                .orElseThrow(() -> new RuntimeException("Shopping cart not found"));

        int totalPrice = shoppingCart
                .getProducts().stream().map(product -> restTemplate
                        .getForObject("http://localhost:8764/api/product/" + product.getId(), HashMap.class))
                .mapToInt(productResponse -> (int) productResponse.get("price")).sum();
        response.put("total_price", Double.toString(totalPrice));

        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<ShoppingCart> getShoppingCartById(Long shoppingCartId) {
        return ResponseEntity.ok().body(shoppingCartRepository.findById(shoppingCartId)
                .orElseThrow(() -> new RuntimeException("Shopping cart not found")));
    }
}
