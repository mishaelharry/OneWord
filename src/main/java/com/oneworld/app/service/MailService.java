/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oneworld.app.service;

import com.oneworld.app.dto.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 *
 * @author hp
 */
@Service
public class MailService {
    
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    TemplateEngine htmlTemplateEngine;
    
    public void sendUserVerifyMail(EmailRequest emailRequest){
        Context context = new Context();
        context.setVariables(emailRequest.getModel());
        String htmlContent = htmlTemplateEngine.process("verify-user", context);
        emailRequest.setMessage(htmlContent);
        sendByJavaMailer(emailRequest);
    }  
    
    public void sendVerifiedMail(EmailRequest emailRequest){
        Context context = new Context();
        context.setVariables(emailRequest.getModel());
        String htmlContent = htmlTemplateEngine.process("verified-user", context);
        emailRequest.setMessage(htmlContent);
        sendByJavaMailer(emailRequest);
    }  
    
    public void sendDeactivateMail(EmailRequest emailRequest){
        Context context = new Context();
        context.setVariables(emailRequest.getModel());
        String htmlContent = htmlTemplateEngine.process("deactivate-user", context);
        emailRequest.setMessage(htmlContent);
        sendByJavaMailer(emailRequest);
    }  
    
    @Async
    private void sendByJavaMailer(EmailRequest emailRequest){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailRequest.getFrom());
        mailMessage.setTo(emailRequest.getTo());
        mailMessage.setSubject(emailRequest.getSubject());
        mailMessage.setText(emailRequest.getMessage());        
        javaMailSender.send(mailMessage);
    }
}
