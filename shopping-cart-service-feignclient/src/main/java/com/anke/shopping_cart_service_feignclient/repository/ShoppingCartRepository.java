package com.anke.shopping_cart_service_feignclient.repository;

import com.anke.shopping_cart_service_feignclient.entity.ShoppingCartFeignClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCartFeignClient, Long> {

}
