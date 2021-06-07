/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oneworld.app.controller;

import com.oneworld.app.dto.BaseResponse;
import com.oneworld.app.dto.PagedResponse;
import com.oneworld.app.dto.UserRequest;
import com.oneworld.app.dto.UserResponse;
import com.oneworld.app.service.UserService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hp
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    public ResponseEntity<BaseResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest userRequest){
                
        return null;
    }
    
    @GetMapping
    public PagedResponse<UserResponse> getUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size){
        return null;
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateUser(@PathVariable("id") String id) {
        
        return null;
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> verifyUser(@PathVariable("id") String id) {
        
        return null;
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deactivateUser(@PathVariable("id") String id) {
        
        return null;
    }
    
    
    
}
