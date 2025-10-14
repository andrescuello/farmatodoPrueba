
package com.farmatodo.challenge.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TokenizeRequest(
        @NotBlank @Pattern(regexp="\\d{13,19}") String number,
        @NotBlank @Size(min=3,max=4) String cvv,
        @NotBlank String exp,
        String customerId
) {}
