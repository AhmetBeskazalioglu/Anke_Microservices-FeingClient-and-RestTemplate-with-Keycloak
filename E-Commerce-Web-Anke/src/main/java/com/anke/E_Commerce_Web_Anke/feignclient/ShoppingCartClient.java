package com.anke.E_Commerce_Web_Anke.feignclient;

import com.anke.E_Commerce_Web_Anke.entity.Product;
import com.anke.E_Commerce_Web_Anke.entity.ShoppingCart;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "shopping-cart-service-feignclient")
public interface ShoppingCartClient {

    @PostMapping("api/shopping-cart-fc/create/empty")
    ResponseEntity<ShoppingCart> createCart();

    @PostMapping("api/shopping-cart-fc/{cartId}/addExistingProducts")
    ResponseEntity<ShoppingCart> addExistingProductsToCart(@RequestBody List<Product> products, @RequestParam("cartId") Long cartId);

    @GetMapping("api/shopping-cart-fc/{id}")
    ResponseEntity<ShoppingCart> getCartById(@PathVariable("id") Long id);

    @PostMapping("api/shopping-cart-fc/{shoppingCartId}/removeProduct/{productId}")
    ResponseEntity<Void> removeProduct(@PathVariable("shoppingCartId") Long shoppingCartId,
                                       @PathVariable("productId") Long productId);

    @PostMapping("api/shopping-cart-fc//{shoppingCartId}/removeAllProducts")
    ResponseEntity<Void> removeAllProducts(@PathVariable("shoppingCartId") Long shoppingCartId);

}


