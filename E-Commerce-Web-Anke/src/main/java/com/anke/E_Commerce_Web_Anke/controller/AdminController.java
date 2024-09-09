package com.anke.E_Commerce_Web_Anke.controller;

import com.anke.E_Commerce_Web_Anke.entity.Product;
import com.anke.E_Commerce_Web_Anke.feignclient.ProductClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private ProductClient productClient;

    @Autowired
    private ProductController productController;

    @GetMapping("/management")
    public String showAdmin(Model model) {
        List<Product> products = productClient.getAllProducts();
        model.addAttribute("products", products);
        return "management/admindashboard";
    }
}
