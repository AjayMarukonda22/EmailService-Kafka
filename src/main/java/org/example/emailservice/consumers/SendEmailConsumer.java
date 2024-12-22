package org.example.emailservice.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.message.SimpleMessage;
import org.example.emailservice.dtos.SendEmailMessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SendEmailConsumer {
 private JavaMailSender javaMailSender;
 private ObjectMapper objectMapper;
 @Autowired
 public SendEmailConsumer(JavaMailSender javaMailSender) {
     this.javaMailSender = javaMailSender;
     this.objectMapper = new ObjectMapper();
 }

@KafkaListener(topics = "sendEmails", groupId = "emailservice")
    public void handleSendEmail(String message) throws JsonProcessingException {
        SendEmailMessageDto email = objectMapper.readValue(message, SendEmailMessageDto.class);

    SimpleMailMessage message1 = new SimpleMailMessage();
    message1.setFrom(email.getFrom());
    message1.setTo(email.getTo());
    message1.setText(email.getBody());
    message1.setSubject(email.getSubject());
    message1.setSentDate(new Date());
    javaMailSender.send(message1);

    System.out.println("email sent succesfully");
    }
}
