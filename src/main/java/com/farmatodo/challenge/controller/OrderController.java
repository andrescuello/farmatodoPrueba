
package com.farmatodo.challenge.controller;

import com.farmatodo.challenge.controller.dto.CreateOrderRequest;
import com.farmatodo.challenge.controller.dto.CreateOrderResponse;
import com.farmatodo.challenge.domain.Order;
import com.farmatodo.challenge.repo.PaymentAttemptRepository;
import com.farmatodo.challenge.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;
    private final PaymentAttemptRepository attempts;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> create(@RequestBody @Valid CreateOrderRequest req) {
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        try {
            Order o = service.createAndPay(req.customerId(), req.shippingAddress(), req.cardToken());
            int count = (int) attempts.findAll().stream().filter(a -> a.getOrderRef().getId().equals(o.getId())).count();
            return ResponseEntity.ok(new CreateOrderResponse(o.getId(), o.getStatus(), count, traceId));
        } finally {
            MDC.remove("traceId");
        }
    }
}
