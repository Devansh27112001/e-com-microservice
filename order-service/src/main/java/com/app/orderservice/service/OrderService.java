package com.app.orderservice.service;

import com.app.orderservice.repository.OrderRepo;
import com.app.orderservice.dto.OrderItemDto;
import com.app.orderservice.dto.OrderResponse;
import com.app.orderservice.model.*;
import com.app.orderservice.model.CartItem;
import com.app.orderservice.model.Order;
import com.app.orderservice.model.OrderItem;
import com.app.orderservice.model.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private CartService cartService;

    public Optional<OrderResponse> createOrder(String userId) {
        // Validate the cart items
        List<CartItem> items = cartService.findCartItemsByUserId(userId);
        if(items.isEmpty()){
            return Optional.empty();
        }
//        // Validate for user
//        Optional<User> userOpt = userRepo.findById(Long.valueOf(userId));
//        if(userOpt.isEmpty()) {
//            return Optional.empty();
//        }
//        User user = userOpt.get();

        // Calculate total price
        BigDecimal totalPrice = items.stream()
                // Instead of .map(item -> item.getPrice()), we did it like this way as the item is of type CartItem
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create order
        Order order = new Order();
        order.setUserId(Long.valueOf(userId));
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);

        // Converting items:List<CartItem>
        List<OrderItem> orderItems = items.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                )).toList();
        order.setItems(orderItems);
        Order savedOrder = orderRepo.save(order);

        // Clear the cart
        cartService.clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order savedOrder) {
        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getTotalAmount(),
                savedOrder.getStatus(),
                savedOrder.getItems()
                        .stream()
                        .map(item -> new OrderItemDto(
                                item.getId(),
                                item.getProductId(),
                                item.getQuantity(),
                                item.getPrice(),
                                item.getPrice().multiply(new BigDecimal(item.getQuantity()))
                        ))
                        .toList(),
                savedOrder.getCreatedAt()
        );
    }
}
