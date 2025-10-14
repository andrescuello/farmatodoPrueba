package com.farmatodo.challenge.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditLog {

  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  @Column(name = "trace_id", nullable = false, length = 64)
  private String traceId;

  @Column(name = "event_type", nullable = false, length = 64)
  private String eventType;

  @Lob
  @Column(columnDefinition = "text")
  private String payload;

  @Column(name = "at", insertable = false, updatable = false)
  private Instant at;
}
