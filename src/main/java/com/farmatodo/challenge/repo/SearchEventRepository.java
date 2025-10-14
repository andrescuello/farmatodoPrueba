
package com.farmatodo.challenge.repo;

import com.farmatodo.challenge.domain.SearchEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SearchEventRepository extends JpaRepository<SearchEvent, UUID> {

}
