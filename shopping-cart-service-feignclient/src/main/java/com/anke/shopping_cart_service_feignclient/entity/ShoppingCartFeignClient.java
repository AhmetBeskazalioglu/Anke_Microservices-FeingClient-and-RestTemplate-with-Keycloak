package com.anke.shopping_cart_service_feignclient.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class ShoppingCartFeignClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shoppincartName;

    @ManyToMany
    @JoinTable(
            name = "shopping_cart_product_feignclient",
            joinColumns = @JoinColumn(name = "shopping_cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShoppincartName() {
        return shoppincartName;
    }

    public void setShoppincartName(String shoppincartName) {
        this.shoppincartName = shoppincartName;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
