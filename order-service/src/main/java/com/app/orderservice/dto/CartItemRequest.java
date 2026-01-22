package com.app.orderservice.dto;

import lombok.Data;

@Data
public class CartItemRequest {

    private Long productId;
    private Integer quantity;
}
