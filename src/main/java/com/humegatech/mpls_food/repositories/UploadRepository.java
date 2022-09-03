package com.humegatech.mpls_food.repositories;

import com.humegatech.mpls_food.domains.Upload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadRepository extends JpaRepository<Upload, Long> {
}
