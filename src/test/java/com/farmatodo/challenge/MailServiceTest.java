package com.farmatodo.challenge;

import com.farmatodo.challenge.domain.Customer;
import com.farmatodo.challenge.domain.Order;
import com.farmatodo.challenge.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MailServiceTest {
    @Mock
    private JavaMailSender sender;

    @InjectMocks
    private MailService mailService;

    @Test
    void shouldSendSuccessEmail() throws MessagingException, NoSuchFieldException, IllegalAccessException {
        Order order = new Order();
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        order.setCustomer(customer);
        order.setId(UUID.randomUUID());

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(sender.createMimeMessage()).thenReturn(mimeMessage);

        Field field = MailService.class.getDeclaredField("username");
        field.setAccessible(true);
        field.set(mailService, "testfarmatodo0@gmail.com");

        mailService.sendSuccess(order);

        verify(sender).send(mimeMessage);
    }

    @Test
    void shouldSendFailureEmail() throws MessagingException, NoSuchFieldException, IllegalAccessException {
        Order order = new Order();
        Customer customer = new Customer();
        customer.setEmail("fail@example.com");
        order.setCustomer(customer);
        order.setId(UUID.randomUUID());

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(sender.createMimeMessage()).thenReturn(mimeMessage);

        Field field = MailService.class.getDeclaredField("username");
        field.setAccessible(true);
        field.set(mailService, "testfarmatodo0@gmail.com");

        mailService.sendFailure(order);

        verify(sender).send(mimeMessage);
    }
}
