
package com.farmatodo.challenge.controller;

import com.farmatodo.challenge.controller.dto.UpsertCustomerRequest;
import com.farmatodo.challenge.domain.Customer;
import com.farmatodo.challenge.repo.CustomerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository repo;

    @GetMapping
    public Page<Customer> list(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        return repo.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> get(@PathVariable UUID id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Customer create(@RequestBody @Valid UpsertCustomerRequest r) {
        Customer c = Customer.builder()
                .name(r.name())
                .email(r.email())
                .phone(r.phone())
                .address(r.address())
                .build();
        return repo.save(c);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable UUID id, @RequestBody @Valid UpsertCustomerRequest r) {
        return repo.findById(id).map(old -> {
            old.setName(r.name());
            old.setEmail(r.email());
            old.setPhone(r.phone());
            old.setAddress(r.address());
            return ResponseEntity.ok(repo.save(old));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
