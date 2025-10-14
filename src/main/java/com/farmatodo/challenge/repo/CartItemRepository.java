
package com.farmatodo.challenge.repo;

import com.farmatodo.challenge.domain.CartItem;
import com.farmatodo.challenge.domain.Customer;
import com.farmatodo.challenge.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    List<CartItem> findByCustomerId(UUID customerId);
    Optional<CartItem> findByCustomerIdAndProductId(UUID customerId, UUID productId);
    long deleteByCustomerId(UUID customerId);
}
