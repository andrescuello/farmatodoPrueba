
package com.farmatodo.challenge.service;

import com.farmatodo.challenge.domain.Customer;
import com.farmatodo.challenge.domain.TokenizedCard;
import com.farmatodo.challenge.repo.CustomerRepository;
import com.farmatodo.challenge.repo.TokenizedCardRepository;
import com.farmatodo.challenge.util.Crypto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenizationService {

    private final Crypto crypto;
    private final TokenizedCardRepository tokenRepo;
    private final CustomerRepository customerRepo;

    @Transactional
    public TokenizedCard tokenize(String number, String cvv, String exp, String customerIdOpt) {
        if (!number.matches("\\d{13,19}")) throw new IllegalArgumentException("PAN inválido");
        String last4 = number.substring(number.length()-4);
        String brand = detectBrand(number);
        String token = "tok_" + UUID.randomUUID();

        TokenizedCard tc = new TokenizedCard();
        tc.setToken(token);
        tc.setLast4(last4);
        tc.setBrand(brand);
        tc.setEncPan(crypto.encrypt(number));

        if (customerIdOpt != null && !customerIdOpt.isBlank()) {
            try {
                UUID cid = UUID.fromString(customerIdOpt);
                Optional<Customer> c = customerRepo.findById(cid);
                c.ifPresent(tc::setCustomer);
            } catch (Exception ignored) {}
        }
        return tokenRepo.save(tc);
    }

    private String detectBrand(String pan) {
        if (pan.startsWith("4")) return "VISA";
        if (pan.matches("^5[1-5].*")) return "MASTERCARD";
        if (pan.matches("^3[47].*")) return "AMEX";
        return "OTHER";
    }
}
