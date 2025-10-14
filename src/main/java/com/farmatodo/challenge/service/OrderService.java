
package com.farmatodo.challenge.service;

import com.farmatodo.challenge.domain.*;
import com.farmatodo.challenge.repo.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartItemRepository carts;
    private final ProductRepository products;
    private final CustomerRepository customers;
    private final OrderRepository orders;
    private final OrderItemRepository orderItems;
    private final PaymentAttemptRepository attempts;
    private final MailService mail;

    @Value("${app.payment.approvalRate:0.8}")
    private double approvalRate;

    @Value("${app.payment.maxRetries:3}")
    private int maxRetries;

    @Transactional
    public Order createAndPay(UUID customerId, String shipping, String cardToken) {
        String traceId = Optional.ofNullable(MDC.get("traceId")).orElse(UUID.randomUUID().toString());
        MDC.put("traceId", traceId);

        try {
            var customer = customers.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("customer not found"));

            var items = new ArrayList<>(carts.findByCustomerId(customerId));
            if (items.isEmpty()) throw new IllegalArgumentException("cart empty");

            Order o = Order.builder()
                    .customer(customer)
                    .shippingAddress(shipping)
                    .status("CREATED")
                    .tokenUsed(cardToken)
                    .createdAt(Instant.now())
                    .build();
            o = orders.save(o);

            Map<UUID, Product> prodMap = products.findAllById(
                    items.stream().map(ci -> ci.getProduct().getId()).collect(Collectors.toSet())
            ).stream().collect(Collectors.toMap(Product::getId, Function.identity()));

            for (CartItem ci : items) {
                Product p = prodMap.get(ci.getProduct().getId());
                if (p.getStock() < ci.getQty())
                    throw new IllegalStateException("stock insuficiente para " + p.getName());
                p.setStock(p.getStock() - ci.getQty());
                products.save(p);

                orderItems.save(OrderItem.builder()
                        .orderRef(o)
                        .product(p)
                        .qty(ci.getQty())
                        .unitPrice(p.getPrice())
                        .createdAt(Instant.now())
                        .build());
            }

            boolean paid = false; int attempt = 0;
            while (attempt < maxRetries && !paid) {
                attempt++;
                paid = Math.random() < approvalRate;
                attempts.save(PaymentAttempt.builder()
                        .orderRef(o)
                        .attemptNo(attempt)
                        .approved(paid)
                        .reason(paid ? "ok" : "rejected")
                        .at(Instant.now())
                        .build());
            }

            if (paid) {
                o.setStatus("PAID"); orders.save(o);
                mail.sendSuccess(o);
            } else {
                o.setStatus("FAILED"); orders.save(o);
                mail.sendFailure(o);
            }

            carts.deleteAll(items);
            return o;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } finally {
            MDC.remove("traceId");
        }
    }
}
