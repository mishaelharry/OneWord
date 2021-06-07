/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oneworld.app.dto;

import lombok.Data;

/**
 *
 * @author hp
 */
@Data
public class UserRequest {
    
    private String title;
    
    private String firstName;
    
    private String lastName;
    
    private String email;
 
    private String mobile;
    
    private String password;
    
    private String role;
            
}
