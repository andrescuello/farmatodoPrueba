
package com.farmatodo.challenge.repo;

import com.farmatodo.challenge.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}
