package com.farmatodo.challenge.controller.dto;

import com.farmatodo.challenge.domain.CartItem;

public record CartItemDto(
        java.util.UUID id,
        java.util.UUID customerId,
        java.util.UUID productId,
        Integer qty
) {
    public static CartItemDto fromEntity(CartItem ci) {
        return new CartItemDto(
                ci.getId(),
                ci.getCustomer().getId(),
                ci.getProduct().getId(),
                ci.getQty()
        );
    }
}
