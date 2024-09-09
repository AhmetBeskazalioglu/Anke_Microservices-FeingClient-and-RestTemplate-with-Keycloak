package com.anke.shopping_cart_service_feignclient.feignclient;

import com.anke.shopping_cart_service_feignclient.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/product/{productId}")
    Product getProductById(@PathVariable("productId") Long productId);

    @PostMapping("/api/product")
    Product createProduct(@RequestBody Product product);

}
