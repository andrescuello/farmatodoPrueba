package com.farmatodo.challenge.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
  name = "cart_items",
  uniqueConstraints = @UniqueConstraint(
    name = "uq_cart_customer_product",
    columnNames = {"customer_id", "product_id"}
  )
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CartItem {

  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "customer_id", nullable = false,
     foreignKey = @ForeignKey(name = "fk_cart_customer"))
  private Customer customer;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "product_id", nullable = false,
     foreignKey = @ForeignKey(name = "fk_cart_product"))
  private Product product;

  @Column(nullable = false)
  private Integer qty;

  @Column(name = "created_at", insertable = false, updatable = false)
  private Instant createdAt;
}
