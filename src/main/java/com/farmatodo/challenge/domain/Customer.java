package com.farmatodo.challenge.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
  name = "customers",
  uniqueConstraints = {
    @UniqueConstraint(name = "uk_customers_email", columnNames = "email"),
    @UniqueConstraint(name = "uk_customers_phone", columnNames = "phone")
  }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer {

  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  @Column(nullable = false, length = 120)
  private String name;

  @Column(nullable = false, length = 255)
  private String email;

  @Column(nullable = false, length = 50)
  private String phone;

  @Column(length = 255)
  private String address;

  @Column(name = "created_at", insertable = false, updatable = false)
  private Instant createdAt;
}
