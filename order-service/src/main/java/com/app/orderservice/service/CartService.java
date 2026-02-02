package com.app.orderservice.service;

import com.app.orderservice.clients.ProductServiceClient;
import com.app.orderservice.dto.ProductResponse;
import com.app.orderservice.repository.CartRepo;
import com.app.orderservice.dto.CartItemRequest;
import com.app.orderservice.dto.CartItemResponse;
import com.app.orderservice.model.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private ProductServiceClient productServiceClient;

    public boolean addToCart(String id, CartItemRequest request) {
        // Look for product
        ProductResponse productResponse = productServiceClient.getProductDetails(request.getProductId());
        if(productResponse == null || productResponse.getStockQuantity() < request.getQuantity()){
            return false;
        }
//
//        User user = userOpt.get();

        CartItem existingCartItem = cartRepo.findByUserIdAndProductId(Long.valueOf(id), request.getProductId());
        if(existingCartItem != null){
            // Update the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartRepo.save(existingCartItem);
        }else{
            // Create new CartItem
            CartItem cartItem = new CartItem();
            cartItem.setUserId(Long.valueOf(id));
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartRepo.save(cartItem);
        }

        return true;
    }

    public boolean deleteItemFromCart(String userId, Long productId) {
        CartItem cartItem = cartRepo.findByUserIdAndProductId(Long.valueOf(userId),productId);
//        Optional<Product> productOpt = productRepo.findById(productId);
//        Optional<User> userOpt = userRepo.findById(Long.valueOf(userId));

        if(cartItem!=null){
            cartRepo.delete(cartItem);
            return true;
        }
        return false;
    }

    public List<CartItem> findCartItemsByUserId(String id){
        //Validation
        return cartRepo.findAllByUserId(Long.valueOf(id));
    }

    public void clearCart(String userId) {
     cartRepo.deleteByUserId(Long.valueOf(userId));
    }
}
