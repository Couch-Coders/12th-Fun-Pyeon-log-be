package com.example.demo.repository;

import com.example.demo.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
<<<<<<< HEAD
    List<Review> findByStoreId(String storeId);
=======
    List<Review> findAllByStoreId(String storeId);

    List<Review> findAllByStoreIdIn(String[] storeIds);

>>>>>>> develop
}
