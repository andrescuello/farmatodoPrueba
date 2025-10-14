
package com.farmatodo.challenge.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateOrderRequest(
        @NotNull UUID customerId,
        @NotBlank String shippingAddress,
        @NotBlank String cardToken
) {}
