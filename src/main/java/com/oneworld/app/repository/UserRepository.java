/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oneworld.app.repository;

import com.oneworld.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author hp
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Boolean existsByMobile(String mobile);

    Boolean existsByEmail(String email);
   
    
    
}
