
package com.farmatodo.challenge;

import com.farmatodo.challenge.domain.AuditLog;
import com.farmatodo.challenge.domain.CartItem;
import com.farmatodo.challenge.domain.Customer;
import com.farmatodo.challenge.domain.Product;
import com.farmatodo.challenge.repo.AuditLogRepository;
import com.farmatodo.challenge.repo.CartItemRepository;
import com.farmatodo.challenge.repo.CustomerRepository;
import com.farmatodo.challenge.repo.ProductRepository;
import com.farmatodo.challenge.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock CartItemRepository carts;
    @Mock CustomerRepository customers;
    @Mock ProductRepository products;
    @Mock AuditLogRepository auditLog;
    @InjectMocks CartService service;

    UUID customerId;
    UUID productId;
    Customer customer;
    Product product;
    AuditLog audit;

    @BeforeEach
    void setup() {
        customerId = UUID.randomUUID();
        productId = UUID.randomUUID();
        customer = Customer.builder().id(customerId).name("John").email("j@test.com").phone("300").build();
        product = Product.builder().id(productId).name("Ibup").price(new BigDecimal("5200")).stock(10).build();
    }

    @Test
    void addItem_newItem_creates() {
        when(customers.findById(customerId)).thenReturn(Optional.of(customer));
        when(products.findById(productId)).thenReturn(Optional.of(product));
        when(carts.findByCustomerIdAndProductId(customerId, productId)).thenReturn(Optional.empty());

        ArgumentCaptor<CartItem> cap = ArgumentCaptor.forClass(CartItem.class);
        when(carts.save(cap.capture())).thenAnswer(i -> i.getArgument(0));

        CartItem saved = service.addItem(customerId, productId, 2);

        assertThat(saved.getQty()).isEqualTo(2);
        assertThat(saved.getCustomer().getId()).isEqualTo(customerId);
        assertThat(saved.getProduct().getId()).isEqualTo(productId);
    }

    @Test
    void addItem_existing_incrementsQty() {
        when(customers.findById(customerId)).thenReturn(Optional.of(customer));
        when(products.findById(productId)).thenReturn(Optional.of(product));
        CartItem existing = CartItem.builder().id(UUID.randomUUID()).customer(customer).product(product).qty(1).build();
        when(carts.findByCustomerIdAndProductId(customerId, productId)).thenReturn(Optional.of(existing));
        when(carts.save(any())).thenAnswer(i -> i.getArgument(0));

        CartItem updated = service.addItem(customerId, productId, 3);
        assertThat(updated.getQty()).isEqualTo(4);
    }

    @Test
    void list_returnsItems() {
        when(carts.findByCustomerId(customerId)).thenReturn(List.of());
        assertThat(service.list(customerId)).isEmpty();
    }

    @Test
    void removeOne_missing_throws() {
        when(carts.findByCustomerIdAndProductId(customerId, productId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.removeOne(customerId, productId));
    }

    @Test
    void clearCart_Ok(){
        UUID customerId = UUID.randomUUID();
        service.clear(customerId);
        verify(carts).deleteByCustomerId(customerId);
    }
}
