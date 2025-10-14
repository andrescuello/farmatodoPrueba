package com.farmatodo.challenge.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
  name = "tokenized_cards",
  uniqueConstraints = @UniqueConstraint(name = "uk_tokenized_cards_token", columnNames = "token")
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TokenizedCard {

  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  @Column(nullable = false, length = 64)
  private String token;

  @Column(nullable = false, length = 4)
  private String last4;

  @Column(nullable = false, length = 32)
  private String brand;

  @Column(name = "enc_pan", nullable = false, columnDefinition = "text")
  private String encPan;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "fk_tokenized_cards_customer"))
  private Customer customer;

  @Column(name = "created_at", insertable = false, updatable = false)
  private Instant createdAt;
}
