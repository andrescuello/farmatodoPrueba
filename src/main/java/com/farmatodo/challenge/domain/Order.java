package com.farmatodo.challenge.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {

  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "customer_id", nullable = false,
     foreignKey = @ForeignKey(name = "fk_orders_customer"))
  private Customer customer;

  @Column(name = "shipping_address", length = 255)
  private String shippingAddress;

  @Column(nullable = false, length = 16)
  private String status;

  @Column(name = "token_used", length = 64)
  private String tokenUsed;

  @Column(name = "created_at", insertable = false, updatable = false)
  private Instant createdAt;
}
