package com.rajasreeit.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpService {


    @Autowired
    private JavaMailSender javaMailSender;

    public String generateOTP() {
        Random rand = new Random();
        System.out.println(rand);
        return String.format("%06d", rand.nextInt(1000000));
    }

    // Log OTP to the console instead of sending it, (using this for testing purpose only)
    public void logOTP(String mobileNumber, String otp) {
        System.out.println("Sending OTP " + otp + " to mobile number: " + mobileNumber);
    }

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }
}
