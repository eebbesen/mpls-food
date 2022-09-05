package com.humegatech.mpls_food.repositories;

import com.humegatech.mpls_food.domains.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {
    List<Deal> findByPlaceId(Long placeId);
}
