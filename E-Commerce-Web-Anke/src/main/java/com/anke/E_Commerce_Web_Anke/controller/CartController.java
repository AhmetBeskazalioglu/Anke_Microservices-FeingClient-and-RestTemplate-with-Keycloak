package com.anke.E_Commerce_Web_Anke.controller;

import com.anke.E_Commerce_Web_Anke.entity.Product;
import com.anke.E_Commerce_Web_Anke.entity.ShoppingCart;
import com.anke.E_Commerce_Web_Anke.feignclient.ProductClient;
import com.anke.E_Commerce_Web_Anke.feignclient.ShoppingCartClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("api/feign-client/cart")
public class CartController {

    @Autowired
    private ProductClient productClient;

    @Autowired
    private ShoppingCartClient shoppingCartClient;

    @Autowired
    private ProductController productController;

    private Long currentCartId = 1L;

    @PostMapping("/add")
    public String addProductsToCart(@RequestParam("productId") Long productId, Model model) {
        Product product = productClient.getProductById(productId);
        shoppingCartClient.addExistingProductsToCart(List.of(product), currentCartId);

        List<Product> products = productClient.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("addedProductId", productId);

        //return "redirect:/api/feign-client/home";
        return productController.getProductsHome(model);
    }

    @GetMapping
    public String viewCart(Model model) {
        ResponseEntity<ShoppingCart> cartById = shoppingCartClient.getCartById(currentCartId);
        model.addAttribute("cart", cartById.getBody());
        return "cart";
    }

    @PostMapping("/{cartId}/product/{productId}/remove")
    public String removeProductFromCart(@PathVariable Long cartId, @PathVariable Long productId,Model model) {
        shoppingCartClient.removeProduct(cartId, productId);
        //return "redirect:/api/feign-client/cart";
        return viewCart(model);
    }

    @PostMapping("/{cartId}/products/remove")
    public String removeAllProductsFromCart(@PathVariable Long cartId,Model model) {
        shoppingCartClient.removeAllProducts(cartId);
        //return "redirect:/api/feign-client/cart";
        return viewCart(model);
    }
}