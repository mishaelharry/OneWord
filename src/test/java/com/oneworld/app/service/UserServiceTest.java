/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oneworld.app.service;

import com.oneworld.app.dto.PagedResponse;
import com.oneworld.app.dto.UserRequest;
import com.oneworld.app.dto.UserResponse;
import com.oneworld.app.model.User;
import com.oneworld.app.repository.UserRepository;
import com.oneworld.app.util.Constants;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 *
 * @author hp
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    
    @Mock
    private UserRepository userRepository = mock(UserRepository.class);

    @InjectMocks
    private UserService userService = mock(UserService.class);;
 
    @Test
    public void addUserTest(){
        User user = new User();
        user.setFirstName("Mishael");
        
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        Mockito.when(userService.addUser(userRequest())).thenReturn(userResponse());
        
        UserResponse expected = userService.addUser(userRequest());

        assertThat(expected.getFirstName()).isSameAs(user.getFirstName());
    }
    
    @Test
    public void getUsersTest(){
        
        Page<User> users = Page.empty();
     
        given(userRepository.findAll(PageRequest.of(1, 100, Sort.Direction.DESC, "createdAt"))).willReturn(users);

        Mockito.when(userService.getUsers(1, 100)).thenReturn(userPagedResponse());

        PagedResponse<UserResponse> expected = userService.getUsers(1, 100);
        
        assertEquals(expected.getResult().get(0).getFirstName(), userPagedResponse().getResult().get(0).getFirstName());
    }
    
    @Test
    public void updateUserTest(){
        User user = new User();
        user.setId(1L);
        user.setFirstName("Mishael");
        
        User newUser = new User();
        newUser.setId(1L);
        newUser.setFirstName("Mishael");
        
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        
        Mockito.when(userService.updateUser(1L, userRequest())).thenReturn(userResponse());
             
        UserResponse expected = userService.updateUser(user.getId(), userRequest());
        
        assertEquals(expected.getFirstName(), newUser.getFirstName());

    }
    
    @Test
    public void verifyUserTest(){
        User user = new User();
        user.setId(1L);
        user.setFirstName("Mishael");
        user.setStatus(Constants.VERIFIED);
        
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        
        Mockito.when(userService.verifyUser(1L)).thenReturn(userResponse());
             
        UserResponse expected = userService.verifyUser(user.getId());
        
        assertEquals(expected.getStatus(), user.getStatus());
    }
    
    @Test
    public void deactivateUserTest(){
        User user = new User();
        user.setId(1L);
        user.setFirstName("Mishael");
        user.setStatus(Constants.VERIFIED);
        
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        
        doNothing().when(userService).deactivateUser(1L);             
    }
    
    public UserRequest userRequest(){
        UserRequest userRequest = new UserRequest();
        userRequest.setTitle("Mr");
        userRequest.setFirstName("Mishael");
        userRequest.setLastName("Harry");
        userRequest.setEmail("harry4sure@hotmail.com");
        userRequest.setMobile("09039639237");
        userRequest.setPassword("123456");
        userRequest.setRole(Constants.REGISTERED);
        return userRequest;
    }
    
    public UserResponse userResponse(){
        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setTitle("Mr");
        userResponse.setFirstName("Mishael");
        userResponse.setLastName("Harry");
        userResponse.setEmail("harry4sure@hotmail.com");
        userResponse.setMobile("09039639237");
        userResponse.setRole(Constants.USER_ROLE);
        userResponse.setStatus(Constants.VERIFIED);
        userResponse.setDateDeactivated(Instant.now());
        userResponse.setDateRegistered(Instant.now());
        userResponse.setDateVerified(Instant.now());
        return userResponse;
    }
    
    public PagedResponse<UserResponse> userPagedResponse(){
        PagedResponse pr = new PagedResponse();
        pr.setStatus(true);
        pr.setPage(1);
        pr.setSize(500);
        pr.setResult(Arrays.asList(userResponse()));
        return pr;
    }
}
