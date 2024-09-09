package com.anke.shopping_cart_service.controller;

import com.anke.shopping_cart_service.entity.Product;
import com.anke.shopping_cart_service.entity.ShoppingCart;
import com.anke.shopping_cart_service.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/shopping-cart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @PostMapping
    public ResponseEntity<ShoppingCart> create(@RequestParam("name") String name) {
        return shoppingCartService.create(name);
    }

    @GetMapping("{id}")
    public ResponseEntity<ShoppingCart> getShoppingCartById(
            @PathVariable("id") Long shoppingCartId) {
        return shoppingCartService.getShoppingCartById(shoppingCartId);
    }

    @PostMapping("{id}")
    public ResponseEntity<ShoppingCart> addProducts(
            @PathVariable("id") Long shoppingCartId, @RequestBody List<Product> products) {
        return shoppingCartService.addProducts(shoppingCartId, products);
    }

    @GetMapping("/price/{id}")
    public ResponseEntity<Map<String, String>> getShoppingCartPrice(
            @PathVariable("id") Long shoppingCartId) {
        return shoppingCartService.getShoppingCartPrice(shoppingCartId);
    }

}
