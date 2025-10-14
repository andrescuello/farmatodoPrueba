
package com.farmatodo.challenge;

import com.farmatodo.challenge.domain.*;
import com.farmatodo.challenge.repo.*;
import com.farmatodo.challenge.service.MailService;
import com.farmatodo.challenge.service.OrderService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock CartItemRepository carts;
    @Mock ProductRepository products;
    @Mock CustomerRepository customers;
    @Mock OrderRepository orders;
    @Mock OrderItemRepository orderItems;
    @Mock PaymentAttemptRepository attempts;
    @Mock MailService mail;
    @InjectMocks OrderService service;

    UUID customerId;
    Customer customer;
    Product product;
    CartItem cartItem;
    UUID productId;

    @BeforeEach
    void setup() {
        customerId = UUID.randomUUID();
        customer = Customer.builder().id(customerId).name("Ana").email("ana@test.com").phone("301").build();
        productId = UUID.randomUUID();
        product = Product.builder().id(productId).name("Aceta").price(new BigDecimal("6500")).stock(5).build();
        cartItem = CartItem.builder().id(UUID.randomUUID()).customer(customer).product(product).qty(2).build();

        // Force approval always
        ReflectionTestUtils.setField(service, "approvalRate", 1.0d);
        ReflectionTestUtils.setField(service, "maxRetries", 3);
    }

    @Test
    void createAndPay_paid_flow_ok() throws MessagingException {
        when(customers.findById(customerId)).thenReturn(Optional.of(customer));
        when(carts.findByCustomerId(customerId)).thenReturn(List.of(cartItem));
        when(products.findAllById(anySet())).thenAnswer(inv -> {
            Set<UUID> ids = inv.getArgument(0);
            return ids.stream().map(id -> product).collect(Collectors.toList());
        });
        when(orders.save(any())).thenAnswer(i -> {
            Order o = i.getArgument(0);
            if (o.getId() == null) o.setId(UUID.randomUUID());
            return o;
        });
        when(orderItems.save(any())).thenAnswer(i -> i.getArgument(0));

        Order o = service.createAndPay(customerId, "Calle 123", "tok_xxx");

        assertThat(o.getStatus()).isEqualTo("PAID");
        assertThat(product.getStock()).isEqualTo(3); // 5 - qty(2)
        verify(attempts, atLeastOnce()).save(any());
        verify(mail).sendSuccess(any(Order.class));
        verify(carts).deleteAll(anyList());
    }
}
