package com.shopMe.demo.repository;

import com.shopMe.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
@Query("Select p from Product p where p.name like %:productName%")
    List<Product> findByProductName(String productName);
}
