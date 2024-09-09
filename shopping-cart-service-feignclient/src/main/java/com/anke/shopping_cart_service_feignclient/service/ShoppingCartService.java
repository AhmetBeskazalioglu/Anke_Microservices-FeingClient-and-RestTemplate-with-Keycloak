package com.anke.shopping_cart_service_feignclient.service;

import com.anke.shopping_cart_service_feignclient.entity.Product;
import com.anke.shopping_cart_service_feignclient.entity.ShoppingCartFeignClient;
import com.anke.shopping_cart_service_feignclient.feignclient.ProductClient;
import com.anke.shopping_cart_service_feignclient.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShoppingCartService {

    @Autowired
    private ProductClient productClient;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    /**
     * Create a new shopping cart
     * @param sc
     * @return
     */
    public ResponseEntity<ShoppingCartFeignClient> createCart(ShoppingCartFeignClient sc){
        shoppingCartRepository.save(sc);
        return ResponseEntity.ok(sc);
    }

    public ResponseEntity<ShoppingCartFeignClient> getCartById(Long id){
        if (shoppingCartRepository.findById(id).isPresent()) {
            return ResponseEntity.ok(shoppingCartRepository.findById(id).get());
        }
        return ResponseEntity.status(404).build();
    }

    /**
     * Add existing products to the shopping cart
     * @param products
     * @param shoppingCartId
     * @return
     */
    public ResponseEntity<ShoppingCartFeignClient> addExistingProductsToCart(List<Product> products, Long shoppingCartId) {

        ShoppingCartFeignClient shoppingCart = shoppingCartRepository.findById(shoppingCartId)
                .orElseThrow(() -> new RuntimeException("Shopping cart not found"));

        products.forEach(product -> {
            Product productFeignClient = productClient.getProductById(product.getId());
            if (productFeignClient != null) {
                shoppingCart.getProducts().add(productFeignClient);
            } else {
                throw new RuntimeException("Product not found with id: " + product.getId());
            }
        });
        shoppingCartRepository.save(shoppingCart);

        return ResponseEntity.ok(shoppingCart);
    }

    /**
     * Add new products to the shopping cart
     * @param products
     * @param shoppingCartId
     * @return
     */
    public ResponseEntity<ShoppingCartFeignClient> addNewProductsToCart(List<Product> products, Long shoppingCartId) {

        ShoppingCartFeignClient shoppingCart = shoppingCartRepository.findById(shoppingCartId)
                .orElseThrow(() -> new RuntimeException("Shopping cart not found"));

        products.forEach(product -> {
            Product productFeignClient = productClient.createProduct(product);
            if (productFeignClient != null) {
                shoppingCart.getProducts().add(productFeignClient);
            } else {
                throw new RuntimeException("Product not found with id: " + product.getId());
            }
        });
        shoppingCartRepository.save(shoppingCart);

        return ResponseEntity.ok(shoppingCart);
    }

    /**
     * Remove a product from the shopping cart
     * @param shoppingCartId
     * @param productId
     * @return
     */
    public ResponseEntity<ShoppingCartFeignClient> removeProduct(Long shoppingCartId, Long productId) {
        ShoppingCartFeignClient shoppingCart = shoppingCartRepository.findById(shoppingCartId)
                .orElseThrow(() -> new RuntimeException("Shopping cart not found"));

        Optional<Product> product = shoppingCart.getProducts().stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst();

        if (product.isPresent()) {
            shoppingCart.getProducts().remove(product.get());
            shoppingCartRepository.save(shoppingCart);
        } else {
            throw new RuntimeException("Product not found with id: " + productId);
        }

        return ResponseEntity.ok(shoppingCart);
    }

    /**
     * Remove all products from the shopping cart
     * @param shoppingCartId
     * @return
     */
    public ResponseEntity<ShoppingCartFeignClient> removeAllProducts(Long shoppingCartId) {
        ShoppingCartFeignClient shoppingCart = shoppingCartRepository.findById(shoppingCartId)
                .orElseThrow(() -> new RuntimeException("Shopping cart not found"));

        shoppingCart.getProducts().clear();
        shoppingCartRepository.save(shoppingCart);

        return ResponseEntity.ok(shoppingCart);
    }

    /**
     * Get the total price of the shopping cart
     * @param id
     * @return
     */
    public ResponseEntity<Map<String,Double>> getShoppingCartPrice(Long id){
        ShoppingCartFeignClient shoppingCart = shoppingCartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shopping cart not found"));
        return ResponseEntity.ok(Map.of("total_price",
                shoppingCart.getProducts().stream().mapToDouble(Product::getPrice).sum()));
    }

    /**
     * Delete the shopping cart
     * @param shoppingCartId
     * @return
     */
    public ResponseEntity<String> deleteCart(Long shoppingCartId) {
        shoppingCartRepository.deleteById(shoppingCartId);
        return ResponseEntity.ok("Shopping cart deleted successfully");
    }
}