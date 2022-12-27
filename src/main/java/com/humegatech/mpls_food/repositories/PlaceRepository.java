package com.humegatech.mpls_food.repositories;

import com.humegatech.mpls_food.domains.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PlaceRepository extends JpaRepository<Place, Long> {
    boolean existsByNameIgnoreCase(String name);

    List<Place> findByIdIn(List<Long> ids);

}
