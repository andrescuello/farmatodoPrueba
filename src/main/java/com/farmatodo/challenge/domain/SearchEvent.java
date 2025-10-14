package com.farmatodo.challenge.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "search_events")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SearchEvent {

  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  @Column(nullable = false, length = 255)
  private String q;

  @Column(name = "customer_email", length = 255)
  private String customerEmail;

  @Column(name = "at", insertable = false, updatable = false)
  private Instant at;
}
