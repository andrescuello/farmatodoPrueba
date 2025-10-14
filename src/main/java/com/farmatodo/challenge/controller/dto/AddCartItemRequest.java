
package com.farmatodo.challenge.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddCartItemRequest(
        @NotNull UUID customerId,
        @NotNull UUID productId,
        @NotNull @Min(1) Integer qty
) {}
