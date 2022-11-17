package com.humegatech.mpls_food.repositories;

import com.humegatech.mpls_food.domains.DealLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealLogRepository extends JpaRepository<DealLog, Long> {
}
