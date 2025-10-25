
package com.farmatodo.challenge.controller;

import com.farmatodo.challenge.controller.dto.UpsertProductRequest;
import com.farmatodo.challenge.domain.Product;
import com.farmatodo.challenge.repo.AuditLogRepository;
import com.farmatodo.challenge.repo.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository repo;

    @GetMapping
    public Page<Product> list(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        return repo.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> get(@PathVariable UUID id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Product create(@RequestBody @Valid UpsertProductRequest r) {
        Product p = Product.builder()
                .name(r.name())
                .description(r.description())
                .price(r.price())
                .stock(r.stock())
                .build();
        return repo.save(p);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable UUID id, @RequestBody @Valid UpsertProductRequest r) {
        return repo.findById(id).map(old -> {
            old.setName(r.name());
            old.setDescription(r.description());
            old.setPrice(r.price());
            old.setStock(r.stock());
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
