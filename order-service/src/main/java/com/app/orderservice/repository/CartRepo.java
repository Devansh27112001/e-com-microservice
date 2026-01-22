package com.app.orderservice.repository;

import com.app.orderservice.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepo extends JpaRepository<CartItem,Long> {


    CartItem findByUserIdAndProductId(Long userId, Long productId);

    void deleteByUserIdAndProductId(Long userId, Long productId);

    List<CartItem> findAllByUserId(Long userId);

    void deleteByUserId(Long userId);
}
