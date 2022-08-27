package com.humegatech.mpls_food.repositories;

import com.humegatech.mpls_food.domains.Day;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayRepository extends JpaRepository<Day, Long> {
}
