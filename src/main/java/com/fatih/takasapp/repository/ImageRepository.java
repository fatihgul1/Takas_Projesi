package com.fatih.takasapp.repository;

import com.fatih.takasapp.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
} 