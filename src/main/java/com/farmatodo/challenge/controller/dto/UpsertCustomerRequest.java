
package com.farmatodo.challenge.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpsertCustomerRequest(
        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank String phone,
        String address
) {}
