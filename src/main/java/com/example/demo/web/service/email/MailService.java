package com.example.demo.web.service.email;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public interface MailService {
    @Async
    void send(String toEmail, String tempPassword);
    @Async
    void sendCode(String toEmail, String tempPassword);
    @Async
    void sendRequest(String bookTitle);
}
