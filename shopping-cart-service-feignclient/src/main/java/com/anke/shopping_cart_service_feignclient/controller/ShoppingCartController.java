package com.anke.shopping_cart_service_feignclient.controller;

import com.anke.shopping_cart_service_feignclient.entity.Product;
import com.anke.shopping_cart_service_feignclient.entity.ShoppingCartFeignClient;
import com.anke.shopping_cart_service_feignclient.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/shopping-cart-fc")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping("/create")
    public ResponseEntity<ShoppingCartFeignClient> createCart(@RequestBody ShoppingCartFeignClient sc) {
        shoppingCartService.createCart(sc);
        return ResponseEntity.ok().body(sc);
    }

    @PostMapping("/create/empty")
    public ResponseEntity<ShoppingCartFeignClient> createCart() {
        ShoppingCartFeignClient sc = new ShoppingCartFeignClient();
        shoppingCartService.createCart(sc);
        return ResponseEntity.ok().body(sc);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShoppingCartFeignClient> getCartById(@PathVariable("id") Long id) {
        return shoppingCartService.getCartById(id);
    }

    @PostMapping("/{shoppingCartId}/addExistingProducts")
    public ResponseEntity<ShoppingCartFeignClient> addExistingProductsToCart(@RequestBody List<Product> products,
                                                                             @PathVariable("shoppingCartId") Long shoppingCartId) {
        shoppingCartService.addExistingProductsToCart(products, shoppingCartId);
        return shoppingCartService.getCartById(shoppingCartId);
    }

    @PostMapping("/{shoppingCartId}/addNewProduct")
    public ResponseEntity<ShoppingCartFeignClient> addNewProductsToCart(@RequestBody List<Product> product,
                                                                       @PathVariable("shoppingCartId") Long shoppingCartId) {
        shoppingCartService.addNewProductsToCart(product, shoppingCartId);
        return shoppingCartService.getCartById(shoppingCartId);
    }

    @PostMapping("/{shoppingCartId}/removeProduct/{productId}")
    public ResponseEntity<ShoppingCartFeignClient> removeProduct(@PathVariable("shoppingCartId") Long shoppingCartId,
                                                                 @PathVariable("productId") Long productId) {
        shoppingCartService.removeProduct(shoppingCartId, productId);
        return shoppingCartService.getCartById(shoppingCartId);
    }

    @PostMapping("/{shoppingCartId}/removeAllProducts")
    public ResponseEntity<ShoppingCartFeignClient> removeAllProducts(@PathVariable("shoppingCartId") Long shoppingCartId) {
        shoppingCartService.removeAllProducts(shoppingCartId);
        return shoppingCartService.getCartById(shoppingCartId);
    }

    @DeleteMapping("/{shoppingCartId}")
    public ResponseEntity<String> deleteCart(@PathVariable("shoppingCartId") Long shoppingCartId) {
        return shoppingCartService.deleteCart(shoppingCartId);
    }

    @GetMapping("/{id}/price")
    public ResponseEntity<Map<String,Double>> getShoppingCartPrice(@PathVariable("id") Long id) {
        return shoppingCartService.getShoppingCartPrice(id);
    }
}