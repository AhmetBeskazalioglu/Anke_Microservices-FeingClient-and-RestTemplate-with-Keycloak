package com.anke.E_Commerce_Web_Anke.entity;

import java.util.Set;

public class ShoppingCart {

    private Long id;
    private String shoppincartName;
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
