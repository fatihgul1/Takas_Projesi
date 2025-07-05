package com.fatih.takasapp.service;

import com.fatih.takasapp.entity.Product;
import com.fatih.takasapp.entity.User;
import com.fatih.takasapp.repository.ProductRepository;
import com.fatih.takasapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public ProductService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Product saveWithImages(Product product, List<MultipartFile> imageFiles) {
        if (product.getOwner() != null && product.getOwner().getId() != null) {
            User user = userRepository.findById(product.getOwner().getId())
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
            product.setOwner(user);
        } else {
            throw new RuntimeException("Owner bilgisi eksik!");
        }

        if (imageFiles != null && !imageFiles.isEmpty()) {
            List<String> urls = new ArrayList<>();
            for (MultipartFile file : imageFiles) {
                try {
                    String originalFilename = file.getOriginalFilename();
                    String extension = "";
                    if (originalFilename != null && originalFilename.contains(".")) {
                        extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    }
                    String uniqueName = UUID.randomUUID() + extension;
                    File path = new File(uploadDir);
                    if (!path.exists()) path.mkdirs();
                    file.transferTo(new File(uploadDir + uniqueName));
                    urls.add(uniqueName);
                } catch (IOException e) {
                    throw new RuntimeException("Resim yüklenemedi!", e);
                }
            }
            if (!urls.isEmpty()) {
                product.setImageUrl(urls.get(0));
            }
        }

        return productRepository.save(product);
    }

    public List<Product> findByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return productRepository.findByOwner(user);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findByIdWithAuth(Long id, Long userId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

        if (!product.getOwner().getId().equals(userId)) {
            throw new RuntimeException("Bu ürüne erişim yetkiniz yok.");
        }
        return product;
    }

    public Product updateProduct(Long id, Product updated, Long userId) {
        Product product = findByIdWithAuth(id, userId);
        product.setName(updated.getName());
        product.setDescription(updated.getDescription());
        product.setCategory(updated.getCategory());
        product.setPrice(updated.getPrice());
        return productRepository.save(product);
    }

    public void deleteProduct(Long id, Long userId) {
        Product product = findByIdWithAuth(id, userId);
        productRepository.delete(product);
    }

    public Product findByIdPublic(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
    }

}
