/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oneworld.app.dto;

import java.time.Instant;
import lombok.Data;

/**
 *
 * @author hp
 */
@Data
public class UserResponse {
    
    private Long id;

    private String title;
    
    private String firstName;
    
    private String lastName;
    
    private String email;
 
    private String mobile;
        
    private String role;
    
    private Instant dateRegistered;
    
    private Integer verified;
    
    private Instant dateVerified;
    
    private Instant dateDeactivated;
    
    private String status;
    
}
