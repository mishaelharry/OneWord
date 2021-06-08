/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oneworld.app.service;

import com.oneworld.app.dto.PagedResponse;
import com.oneworld.app.dto.UserRequest;
import com.oneworld.app.dto.UserResponse;
import com.oneworld.app.exception.BadRequestException;
import com.oneworld.app.model.User;
import com.oneworld.app.repository.UserRepository;
import static com.oneworld.app.util.Constant.DEACTIVATED;
import static com.oneworld.app.util.Constant.REGISTERED;
import static com.oneworld.app.util.Constant.VERIFIED;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public UserResponse addUser(UserRequest userRequest) {
        UserResponse userResponse = null;
        if(userRequest != null){
            User user = new User();
            BeanUtils.copyProperties(userRequest, user);
            user.setStatus(REGISTERED);
            user.setDateRegistered(Instant.now());
            User result = userRepository.save(user);
            if(result != null){
                userResponse = new UserResponse();
                BeanUtils.copyProperties(result, userResponse);
            }
        }
        return userResponse;
    }

    public PagedResponse<UserResponse> getUsers(int page, int size) {
        validatePageNumberAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<User> users = userRepository.findAll(pageable);
        if (users.getNumberOfElements() == 0) {
            return new PagedResponse<>(false, Collections.emptyList(), users.getNumber(),
                    users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
        }
        
        List<UserResponse> userResponses = users.map(user -> {
            UserResponse userResponse = new UserResponse();
            BeanUtils.copyProperties(user, userResponse);
            return userResponse;
        }).getContent();
        
        return new PagedResponse<>(true, userResponses, users.getNumber(),
                users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
    }
    
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        UserResponse userResponse = null;
        User user = userRepository.findById(id).orElse(null);
        if(user != null){
            if(!StringUtils.isBlank(userRequest.getTitle())){
                user.setTitle(userRequest.getTitle());
            }
            if(!StringUtils.isBlank(userRequest.getFirstName())){
                user.setFirstName(userRequest.getFirstName());
            }
            if(!StringUtils.isBlank(userRequest.getLastName())){
                user.setLastName(userRequest.getLastName());
            }
            if(!StringUtils.isBlank(userRequest.getMobile())){
                user.setMobile(userRequest.getMobile());
            }
            if(!StringUtils.isBlank(userRequest.getRole())){
                user.setRole(userRequest.getRole());
            }
            User result = userRepository.save(user);
            if(result != null){
                userResponse = new UserResponse();
                BeanUtils.copyProperties(result, userResponse);
            }
        }        
        return userResponse;
    }
    
    public static void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > 2000) {
            throw new BadRequestException("Page size must not be greater than " + 2000);
        }
    }

    public UserResponse verifyUser(Long id) {
        UserResponse userResponse = null;
        User user = userRepository.findById(id).orElse(null);
        if(user != null){
            user.setVerified(0);
            user.setStatus(VERIFIED);
            user.setDateVerified(Instant.now());
            User result = userRepository.save(user);
            if(result != null){
                userResponse = new UserResponse();
                BeanUtils.copyProperties(result, userResponse);
            }
        }
        return userResponse;
    }

    public void deactivateUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if(user != null){
            user.setVerified(0);
            user.setStatus(DEACTIVATED);
            user.setDateDeactivated(Instant.now());
            userRepository.save(user);
        }
    }
    
}
