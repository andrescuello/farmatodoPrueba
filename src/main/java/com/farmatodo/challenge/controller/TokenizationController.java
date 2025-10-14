
package com.farmatodo.challenge.controller;

import com.farmatodo.challenge.controller.dto.TokenizeRequest;
import com.farmatodo.challenge.controller.dto.TokenizeResponse;
import com.farmatodo.challenge.domain.TokenizedCard;
import com.farmatodo.challenge.service.TokenizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth/tokenize")
@RequiredArgsConstructor
public class TokenizationController {

    private final TokenizationService service;

    @PostMapping
    public ResponseEntity<TokenizeResponse> tokenize(@RequestBody @Valid TokenizeRequest req) {
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        try {
            TokenizedCard tc = service.tokenize(req.number(), req.cvv(), req.exp(), req.customerId());
            return ResponseEntity.ok(new TokenizeResponse(tc.getToken(), tc.getLast4(), tc.getBrand(), traceId));
        } finally {
            MDC.remove("traceId");
        }
    }
}
