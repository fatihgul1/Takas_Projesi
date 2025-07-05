package com.fatih.takasapp.controller;

import com.fatih.takasapp.entity.Product;
import com.fatih.takasapp.entity.User;
import com.fatih.takasapp.repository.ProductRepository;
import com.fatih.takasapp.repository.UserRepository;
import com.fatih.takasapp.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {


    private final ProductService productService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ProductController(ProductService productService, UserRepository userRepository, ProductRepository productRepository) {
        this.productService = productService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> addProduct(
            @RequestPart("product") String productJson,
            @RequestPart("imageFiles") List<MultipartFile> imageFiles) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Product product = objectMapper.readValue(productJson, Product.class);
            Product saved = productService.saveWithImages(product, imageFiles);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    public List<Product> getProductsByUser(@PathVariable Long userId) {
        return productService.findByUserId(userId);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,
                                                 @RequestBody Product updatedProduct,
                                                 Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        Long userId = user.getId();
        return ResponseEntity.ok(productService.updateProduct(id, updatedProduct, userId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        Long userId = user.getId();
        productService.deleteProduct(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductPublic(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findByIdPublic(id));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<Product>> getMyProducts(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        List<Product> myProducts = productService.findByUserId(user.getId());
        return ResponseEntity.ok(myProducts);
    }

    @GetMapping("/active")
    public List<Product> getActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    @PostMapping("/favorites/{productId}")
    public ResponseEntity<?> addFavorite(@PathVariable Long productId, Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

        user.getFavorites().add(product);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/favorites/{productId}")
    public ResponseEntity<?> removeFavorite(@PathVariable Long productId, Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

        user.getFavorites().remove(product);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<Product>> getFavorites(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return ResponseEntity.ok(new ArrayList<>(user.getFavorites()));
    }


}