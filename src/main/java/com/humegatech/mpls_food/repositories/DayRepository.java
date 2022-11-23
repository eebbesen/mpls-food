package com.humegatech.mpls_food.repositories;

import com.humegatech.mpls_food.domains.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface DayRepository extends JpaRepository<Day, Long> {
    List<Day> findByDayOfWeek(DayOfWeek dayOfWeek);

    @Query("SELECT a " +
            "  FROM Day a, " +
            "       Deal e " +
            " WHERE a.deal = e.id" +
            "   AND (e.startDate IS NULL OR e.startDate <= CURRENT_DATE) " +
            "   AND (e.endDate IS NULL OR e.endDate >= CURRENT_DATE)"
    )
    List<Day> findAllActive();
}
