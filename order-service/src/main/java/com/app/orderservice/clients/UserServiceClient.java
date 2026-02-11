package com.app.orderservice.clients;

import com.app.orderservice.dto.UserResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface UserServiceClient {

    @GetExchange(value = "/api/users/{id}",accept = "application/json")
    UserResponse getUserById(@PathVariable String id);
}
