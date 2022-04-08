package com.tuturial.apidemo.Spring.tuturial.repositories;

import com.tuturial.apidemo.Spring.tuturial.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByProductName(String productName);
}
