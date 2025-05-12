package com.fatih.takasapp.service;

import com.fatih.takasapp.entity.Product;
import com.fatih.takasapp.entity.User;
import com.fatih.takasapp.repository.ProductRepository;
import com.fatih.takasapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductService(ProductRepository productRepository,UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Product save(Product product) {
        if (product.getOwner() != null && product.getOwner().getId() != null) {
            User user = userRepository.findById(product.getOwner().getId())
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
            product.setOwner(user);
        } else {
            throw new RuntimeException("Owner bilgisi eksik!");
        }

        return productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
