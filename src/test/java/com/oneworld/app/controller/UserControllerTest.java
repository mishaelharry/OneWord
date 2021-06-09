/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oneworld.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneworld.app.dto.PagedResponse;
import com.oneworld.app.dto.UserRequest;
import com.oneworld.app.dto.UserResponse;
import com.oneworld.app.exception.UserNotFoundException;
import com.oneworld.app.service.UserService;
import com.oneworld.app.util.Constants;
import com.oneworld.app.util.JsonUtil;
import java.time.Instant;
import java.util.Arrays;
import static org.hamcrest.CoreMatchers.is;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author hp
 */
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    public void createUserTest() throws Exception {        
        given(userService.addUser(userRequest())).willReturn(userResponse());
        
        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(userRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.firstName", is(userRequest().getFirstName())));
                
    }
    
    @Test
    public void getUsersTest() throws Exception {
        given(userService.getUsers(1, 100)).willReturn(userPagedResponse());
        mockMvc.perform(get("/api/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    public void updateUserTest() throws Exception {
        given(userService.updateUser(1L, userRequest())).willReturn(userResponse());
        
        mockMvc.perform(put("/api/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(userRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.firstName", is(userRequest().getFirstName())));        
    }
    
    @Test
    public void updateUserDoesntExistTest() throws Exception {
        Mockito.doThrow(new UserNotFoundException(1L)).when(userService).updateUser(1L, userRequest());
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(put("/api/user/1")
                .content(mapper.writeValueAsString(userRequest()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());                
    }
    
    @Test
    public void verifyUserTest() throws Exception {
        given(userService.verifyUser(1L)).willReturn(userResponse());
        
        mockMvc.perform(get("/api/user/verify/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(userRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.firstName", is(userRequest().getFirstName())));      
    }
    
    @Test
    public void verifyUserDoesntExistTest() throws Exception {
        Mockito.doThrow(new UserNotFoundException(1L)).when(userService).verifyUser(1L);
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(get("/api/user/verify/1")
                .content(mapper.writeValueAsString(userRequest()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());                
    }
    
    @Test
    public void deactivateUserTest() throws Exception {
        doNothing().when(userService).deactivateUser(1L);
        
        mockMvc.perform(delete("/api/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    public void deactivateUserDoesntExistTest() throws Exception {
        Mockito.doThrow(new UserNotFoundException(1L)).when(userService).deactivateUser(1L);
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(delete("/api/user/1")
                .content(mapper.writeValueAsString(userRequest()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());                
    }
    
    
    public UserRequest userRequest(){
        UserRequest userRequest = new UserRequest();
        userRequest.setTitle("Mr");
        userRequest.setFirstName("Mishael");
        userRequest.setLastName("Harry");
        userRequest.setEmail("harry4sure@hotmail.com");
        userRequest.setMobile("09039639237");
        userRequest.setPassword("123456");
        userRequest.setRole(Constants.USER_ROLE);
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
        userResponse.setRole(Constants.REGISTERED);
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
