package com.anke.E_Commerce_Web_Anke.controller;

import com.anke.E_Commerce_Web_Anke.entity.Product;
import com.anke.E_Commerce_Web_Anke.feignclient.ProductClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/api/feign-client")
public class ProductController {

    @Autowired
    private ProductClient productClient;

    @GetMapping("/products")
    public String getProducts(Model model) {
        List<Product> products = productClient.getAllProducts();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/home")
    public String getProductsHome(Model model) {
        List<Product> products = productClient.getAllProducts();
        model.addAttribute("products", products);
        return "home";
    }

    @PostMapping("/showFormForAdd")
    public String createProduct(Model model) {
        List<Product> products = productClient.getAllProducts();
        model.addAttribute("products", products);

        Product product = new Product();
        model.addAttribute("product", product);

        return "management/addproduct";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product,Model model) {
        productClient.createProduct(product);
        return createProduct(model);
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@ModelAttribute("id") Long id, Model model) {
        productClient.deleteProduct(id);
        return createProduct(model);
    }



}
