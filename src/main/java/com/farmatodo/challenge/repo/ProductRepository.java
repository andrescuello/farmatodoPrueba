
package com.farmatodo.challenge.repo;

import com.farmatodo.challenge.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

}
