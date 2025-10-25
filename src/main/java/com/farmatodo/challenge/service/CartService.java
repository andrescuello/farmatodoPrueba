
package com.farmatodo.challenge.service;

import com.farmatodo.challenge.domain.AuditLog;
import com.farmatodo.challenge.domain.CartItem;
import com.farmatodo.challenge.domain.Customer;
import com.farmatodo.challenge.domain.Product;
import com.farmatodo.challenge.repo.AuditLogRepository;
import com.farmatodo.challenge.repo.CartItemRepository;
import com.farmatodo.challenge.repo.CustomerRepository;
import com.farmatodo.challenge.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository carts;
    private final CustomerRepository customers;
    private final ProductRepository products;
    private final AuditLogRepository auditRepo;

    @Transactional
    public CartItem addItem(UUID customerId, UUID productId, int qty) {
        var customer = customers.findById(customerId).orElseThrow(() -> new IllegalArgumentException("customer not found"));
        var product = products.findById(productId).orElseThrow(() -> new IllegalArgumentException("product not found"));
        var existing = carts.findByCustomerIdAndProductId(customerId, productId);
        AuditLog audit = AuditLog.builder()
                .traceId(UUID.randomUUID().toString())
                .eventType("add items").build();
        if (existing.isPresent()) {
            var ci = existing.get();
            ci.setQty(ci.getQty() + qty);
            audit.setPayload(ci.toString());
            auditRepo.save(audit);
            return carts.save(ci);
        }
        CartItem ci = CartItem.builder().customer(customer).product(product).qty(qty).build();
        audit.setPayload(ci.toString());
        auditRepo.save(audit);
        return carts.save(ci);
    }

    public List<CartItem> list(UUID customerId) {
        return carts.findByCustomerId(customerId);
    }

    @Transactional
    public void removeOne(UUID customerId, UUID productId) {
        var existing = carts.findByCustomerIdAndProductId(customerId, productId)
                .orElseThrow(() -> new IllegalArgumentException("item not found"));
        carts.delete(existing);
    }

    @Transactional
    public void clear(UUID customerId) {
        carts.deleteByCustomerId(customerId);
    }
}
