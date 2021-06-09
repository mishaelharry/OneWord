/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oneworld.app.model;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;

/**
 *
 * @author hp
 */
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = {
        "mobile"
    }),
    @UniqueConstraint(columnNames = {
        "email"
    })
})
@Data
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "email")
    private String email;
 
    @Column(name = "mobile")
    private String mobile;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "role")
    private String role;
    
    @Column(name="date_registered", nullable=false, updatable=false)
    private Instant dateRegistered;
    
    @Column(name = "verified")
    private Integer verified;
    
    @Column(name="date_verified")
    private Instant dateVerified;
    
    @Column(name="date_deactivated")
    private Instant dateDeactivated;
    
    @Column(name = "status")
    private String status;
    
}
