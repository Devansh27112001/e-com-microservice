package com.app.userserviceecom.controller;

import com.app.userserviceecom.dto.UserRequest;
import com.app.userserviceecom.dto.UserResponse;
import com.app.userserviceecom.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
//    private static Logger logger = LoggerFactory.getLogger(UserController.class);


    @GetMapping("")
    public ResponseEntity<List<UserResponse>> allUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("")
    public ResponseEntity<String> addUser(@RequestBody UserRequest userRequest){
        userService.addUser(userRequest);
        return new ResponseEntity<>("User added successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id){
        log.info("Request received for user: {}",id);
        log.trace("THIS IS TRACE LEVEL - Very detailed logs" );
        log.debug("THIS IS DEBUG LEVEL - Used for development purposes" );
        log.warn("THIS IS WARN LEVEL - Something might be wrong" );
        log.error("THIS IS ERROR LEVEL - Something failed");
        return new ResponseEntity<>(userService.getUserById(id),  HttpStatus.OK);

    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id,@RequestBody UserRequest
            userRequest){
        boolean updated = userService.updateUserById(id, userRequest);
        return updated ? ResponseEntity.ok("Updated successfully") : ResponseEntity.notFound().build();
    }
}
