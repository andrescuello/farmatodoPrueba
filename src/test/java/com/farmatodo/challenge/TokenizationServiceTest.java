package com.farmatodo.challenge;

import com.farmatodo.challenge.domain.AuditLog;
import com.farmatodo.challenge.domain.Customer;
import com.farmatodo.challenge.domain.TokenizedCard;
import com.farmatodo.challenge.repo.AuditLogRepository;
import com.farmatodo.challenge.repo.CustomerRepository;
import com.farmatodo.challenge.repo.TokenizedCardRepository;
import com.farmatodo.challenge.service.TokenizationService;
import com.farmatodo.challenge.util.Crypto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenizationServiceTest {
    @Mock
    private Crypto crypto;
    @Mock
    private TokenizedCardRepository tokenRepo;
    @Mock
    private CustomerRepository customerRepo;
    @Mock
    private AuditLogRepository auditRepo;

    @InjectMocks
    private TokenizationService service;

    private final String validCard = "4111111111111111"; // Visa test number

    @BeforeEach
    void setup() {
        lenient().when(crypto.encrypt(anyString())).thenReturn("encrypted-pan");
    }

    @Test
    void shouldTokenizeCardSuccessfully() {
        // given
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setEmail("test@example.com");

        when(customerRepo.findById(customerId)).thenReturn(Optional.of(customer));

        TokenizedCard saved = new TokenizedCard();
        saved.setId(UUID.randomUUID());
        saved.setToken("tok_123");
        when(tokenRepo.save(any(TokenizedCard.class))).thenReturn(saved);

        TokenizedCard result = service.tokenize(validCard, "123", "12/28", customerId.toString());

        assertNotNull(result);
        assertEquals("tok_123", result.getToken());

        verify(crypto).encrypt(validCard);

        verify(auditRepo).save(argThat(a -> a.getEventType().equals("Tokenize")));

        verify(tokenRepo).save(any(TokenizedCard.class));
    }

    @Test
    void shouldThrowExceptionWhenPanIsInvalid() {

        String invalidCard = "12345";


        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.tokenize(invalidCard, "123", "12/28", null)
        );

        assertEquals("PAN inválido", ex.getMessage());

        verifyNoInteractions(tokenRepo, auditRepo, customerRepo);
    }

    @Test
    void shouldHandleInvalidCustomerIdGracefully() {

        String invalidUUID = "not-a-uuid";

        TokenizedCard mockSaved = new TokenizedCard();
        mockSaved.setToken("tok_123");
        when(tokenRepo.save(any(TokenizedCard.class))).thenReturn(mockSaved);

        TokenizedCard result = service.tokenize(validCard, "123", "12/28", invalidUUID);

        assertNotNull(result);
        verify(auditRepo).save(any(AuditLog.class));
        verify(tokenRepo).save(any(TokenizedCard.class));
        verify(customerRepo, never()).findById(any());
    }
}
