package com.buxxy.buxxy_fraud_engine.otp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class  EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOtp(String toEmail,String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Transaction OTP");
        message.setText("Your OTP is: " + otp + ". It is valid for 5 minutes.");
        javaMailSender.send(message);

    }
    }

