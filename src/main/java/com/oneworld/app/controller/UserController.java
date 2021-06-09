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
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    public ResponseEntity<BaseResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest userRequest){
        if(userService.existsByMobile(userRequest.getMobile())){
            return new ResponseEntity(new BaseResponse(false, "Mobile already exist", null),
                    HttpStatus.OK);
        }
        
        if(userService.existsByEmail(userRequest.getEmail())){
            return new ResponseEntity(new BaseResponse(false, "Email already exist", null), 
                    HttpStatus.OK);
        }
        
        UserResponse userResponse = userService.addUser(userRequest);
        if(userResponse != null){
            return new ResponseEntity(new BaseResponse(true, "User created successfully", userResponse),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity(new BaseResponse(false, "Failed to create user", null),
                    HttpStatus.OK);
        }
    }
    
    @GetMapping
    public PagedResponse<UserResponse> getUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size){
        return userService.getUsers(page, size);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.updateUser(id, userRequest);
        if(userResponse != null){
            return new ResponseEntity(new BaseResponse(true, "User updated successfully", userResponse),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity(new BaseResponse(false, "Failed to updated user", null),
                    HttpStatus.OK);
        }
    }
    
    @GetMapping("/verify/{id}")
    public ResponseEntity<String> verifyUser(@PathVariable("id") Long id) {
        UserResponse userResponse = userService.verifyUser(id);
        if(userResponse != null){
            return ResponseEntity.ok().body("User verified successfully");
        } else {
            return ResponseEntity.ok().body("Failed to verifed user");
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deactivateUser(@PathVariable("id") Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok(new BaseResponse(true, "User deleted.",
                null
        ));
    }
        
}
