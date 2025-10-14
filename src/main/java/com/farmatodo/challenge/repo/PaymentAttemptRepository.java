
package com.farmatodo.challenge.repo;

import com.farmatodo.challenge.domain.PaymentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentAttemptRepository extends JpaRepository<PaymentAttempt, UUID> {
}
