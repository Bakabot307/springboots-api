package com.shopMe.demo.repository;

import com.shopMe.demo.model.Cart;
import com.shopMe.demo.model.Product;
import com.shopMe.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findAllByUserOrderByCreatedDateDesc(User user);

    void deleteByUser(User user);

    @Query("select c from Cart c where c.product.id = ?1")
    Cart findByProductId(Integer productId);
}
