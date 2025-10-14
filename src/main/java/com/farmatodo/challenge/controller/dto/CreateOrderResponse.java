
package com.farmatodo.challenge.controller.dto;

import java.util.UUID;

public record CreateOrderResponse(
        UUID orderId,
        String status,
        int attempts,
        String traceId
) {}
