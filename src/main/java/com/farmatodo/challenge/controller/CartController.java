
package com.farmatodo.challenge.controller;

import com.farmatodo.challenge.controller.dto.AddCartItemRequest;
import com.farmatodo.challenge.controller.dto.CartItemDto;
import com.farmatodo.challenge.domain.CartItem;
import com.farmatodo.challenge.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService service;

    @PostMapping("/items")
    public CartItem add(@RequestBody @Valid AddCartItemRequest req) {
        return service.addItem(req.customerId(), req.productId(), req.qty());
    }

    @GetMapping("/{customerId}")
    public List<CartItemDto> list(@PathVariable UUID customerId) {

        return service.list(customerId).stream()
                .map(CartItemDto::fromEntity)
                .toList();
    }

    @DeleteMapping("/{customerId}/items/{productId}")
    public ResponseEntity<Void> remove(@PathVariable UUID customerId, @PathVariable UUID productId) {
        service.removeOne(customerId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> clear(@PathVariable UUID customerId) {
        service.clear(customerId);
        return ResponseEntity.noContent().build();
    }
}
