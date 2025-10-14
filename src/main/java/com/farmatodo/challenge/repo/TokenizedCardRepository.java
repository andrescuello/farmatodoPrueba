
package com.farmatodo.challenge.repo;

import com.farmatodo.challenge.domain.TokenizedCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TokenizedCardRepository extends JpaRepository<TokenizedCard, UUID> {

}
