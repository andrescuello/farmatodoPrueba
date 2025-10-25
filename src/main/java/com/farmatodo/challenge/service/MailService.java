
package com.farmatodo.challenge.service;

import com.farmatodo.challenge.domain.Order;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender sender;

    @Value("${spring.mail.username:testfarmatodo0@gmail.com}")
    private String username;

    public void sendSuccess(Order o) throws MessagingException {
        MimeMessage mm = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mm,false,"UTF-8");
        helper.setFrom(username);
        helper.setTo(o.getCustomer().getEmail());
        helper.setSubject("Pago aprobado");
        helper.setText("Tu pedido " + o.getId() + " fue aprobado. ¡Gracias!",false);
        sender.send(mm);
    }

    public void sendFailure(Order o) throws MessagingException {
        MimeMessage mm = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mm,false,"UTF-8");
        helper.setFrom(username);
        helper.setTo(o.getCustomer().getEmail());
        helper.setSubject("Pago rechazado");
        helper.setText("Tu pedido " + o.getId() + " fue rechazado. Intenta nuevamente.",false);
        sender.send(mm);
    }
}
