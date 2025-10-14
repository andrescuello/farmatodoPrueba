
package com.farmatodo.challenge.repo;

import com.farmatodo.challenge.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
