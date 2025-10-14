package com.farmatodo.challenge.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
  name = "payment_attempts",
  uniqueConstraints = @UniqueConstraint(
    name = "uq_payment_attempt",
    columnNames = {"order_id", "attempt_no"}
  )
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentAttempt {

  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "order_id", nullable = false,
     foreignKey = @ForeignKey(name = "fk_payment_attempts_order"))
  private Order orderRef;

  @Column(name = "attempt_no", nullable = false)
  private Integer attemptNo;

  @Column(nullable = false)
  private Boolean approved;

  @Column(length = 64)
  private String reason;

  @Column(name = "at", insertable = false, updatable = false)
  private Instant at;
}
