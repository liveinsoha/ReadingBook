package com.example.demo.web.service.email;

import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;

    /**
     * 임시 비밀번호를 전송하는 메소드
     *
     * @param toEmail
     * @param tempPassword
     */
    @Override
    public void send(String toEmail, String tempPassword) {
        MimeMessage message = createMessage(toEmail, tempPassword);
        javaMailSender.send(message);
    }

    private MimeMessage createMessage(String toEmail, String tempPassword) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setTo(toEmail);
            messageHelper.setSubject("[bookshop] 비밀번호 변경 결과입니다.");

            String sendMessage = "";
            sendMessage += "<h2>임시 비밀번호 재설정 결과</h2>";
            sendMessage += "<strong>임시 비밀번호 : </strong>";
            sendMessage += "<span>"+tempPassword+"</span>";
            message.setText(sendMessage, "utf-8", "html");

        } catch (MessagingException e) {
            throw new BaseException(BaseResponseCode.EMAIL_ERROR_OCCURRED);
        }

        return message;
    }
}
