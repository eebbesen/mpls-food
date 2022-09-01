package com.humegatech.mpls_food.repositories;

import com.humegatech.mpls_food.domains.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayRepository extends JpaRepository<Day, Long> {
}
