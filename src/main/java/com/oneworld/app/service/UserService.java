/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oneworld.app.service;

import com.oneworld.app.dto.EmailRequest;
import com.oneworld.app.dto.PagedResponse;
import com.oneworld.app.dto.UserRequest;
import com.oneworld.app.dto.UserResponse;
import com.oneworld.app.exception.BadRequestException;
import com.oneworld.app.exception.UserNotFoundException;
import com.oneworld.app.model.User;
import com.oneworld.app.repository.UserRepository;
import static com.oneworld.app.util.Constants.DEACTIVATED;
import static com.oneworld.app.util.Constants.REGISTERED;
import static com.oneworld.app.util.Constants.VERIFIED;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private MailService mailService;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Value("${app.reset.link}")
    private String resetLink;

    public boolean existsByMobile(String mobile) {
        return userRepository.existsByMobile(mobile);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserResponse addUser(UserRequest userRequest) {
        UserResponse userResponse = null;
        if (userRequest != null) {
            User user = new User();
            BeanUtils.copyProperties(userRequest, user);
            user.setStatus(REGISTERED);
            user.setVerified(0);
            user.setDateRegistered(Instant.now());
            User result = userRepository.save(user);
            if (result != null) {
                userResponse = new UserResponse();
                BeanUtils.copyProperties(result, userResponse);

                Map<String, Object> model = new HashMap<>();
                model.put("first_name", result.getFirstName());
                model.put("link", resetLink + result.getId());
                EmailRequest emailRequest = new EmailRequest();
                emailRequest.setTo(result.getEmail());
                emailRequest.setFrom(fromMail);
                emailRequest.setSubject("Verify your Account");
                emailRequest.setModel(model);
                mailService.sendUserVerifyMail(emailRequest);

            }
        }
        return userResponse;
    }

    public PagedResponse<UserResponse> getUsers(int page, int size) {
        validatePageNumberAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "dateRegistered");

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
        if (user != null) {
            if (!StringUtils.isBlank(userRequest.getTitle())) {
                user.setTitle(userRequest.getTitle());
            }
            if (!StringUtils.isBlank(userRequest.getFirstName())) {
                user.setFirstName(userRequest.getFirstName());
            }
            if (!StringUtils.isBlank(userRequest.getLastName())) {
                user.setLastName(userRequest.getLastName());
            }
            if (!StringUtils.isBlank(userRequest.getMobile())) {
                user.setMobile(userRequest.getMobile());
            }
            if (!StringUtils.isBlank(userRequest.getRole())) {
                user.setRole(userRequest.getRole());
            }
            User result = userRepository.save(user);
            if (result != null) {
                userResponse = new UserResponse();
                BeanUtils.copyProperties(result, userResponse);
            }
        } else {
            new UserNotFoundException(id);
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
        if (user != null) {
            user.setVerified(1);
            user.setStatus(VERIFIED);
            user.setDateVerified(Instant.now());
            User result = userRepository.save(user);
            if (result != null) {
                userResponse = new UserResponse();
                BeanUtils.copyProperties(result, userResponse);

                Map<String, Object> model = new HashMap<>();
                model.put("first_name", result.getFirstName());
                EmailRequest emailRequest = new EmailRequest();
                emailRequest.setTo(result.getEmail());
                emailRequest.setFrom(fromMail);
                emailRequest.setSubject("Account Verified");
                emailRequest.setModel(model);
                mailService.sendVerifiedMail(emailRequest);
            }
        } else {
            new UserNotFoundException(id);
        }
        return userResponse;
    }

    public void deactivateUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setStatus(DEACTIVATED);
            user.setDateDeactivated(Instant.now());
            userRepository.save(user);
            Map<String, Object> model = new HashMap<>();
            model.put("first_name", user.getFirstName());
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setTo(user.getEmail());
            emailRequest.setFrom(fromMail);
            emailRequest.setSubject("User Deactivated");
            emailRequest.setModel(model);
            mailService.sendDeactivateMail(emailRequest);
        } else {
            new UserNotFoundException(id);
        }
    }

}
