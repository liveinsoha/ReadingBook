package com.example.demo.web.service.email;

public interface MailService {
    void send(String toEmail, String tempPassword);

    void sendCode(String toEmail, String tempPassword);
}
