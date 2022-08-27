package com.humegatech.mpls_food.repositories;

import com.humegatech.mpls_food.domains.Place;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlaceRepository extends JpaRepository<Place, Long> {

    boolean existsByNameIgnoreCase(String name);

}
