
package com.farmatodo.challenge.repo;

import com.farmatodo.challenge.domain.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

}
