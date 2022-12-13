package com.example.demo.repository;

import com.example.demo.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByStoreId(Pageable pageable, String storeId);
    List<Review> findByStoreId(String storeId);

    Optional<Long> countByStoreId(String storeId);

}
