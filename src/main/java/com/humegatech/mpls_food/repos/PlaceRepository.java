package com.humegatech.mpls_food.repos;

import com.humegatech.mpls_food.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlaceRepository extends JpaRepository<Place, Long> {

    boolean existsByNameIgnoreCase(String name);

}
