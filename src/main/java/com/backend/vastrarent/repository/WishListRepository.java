package com.backend.vastrarent.repository;

import com.backend.vastrarent.model.Product;
import com.backend.vastrarent.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    List<WishList> findByUser_Id(Long id);
    List<WishList> findByProduct_Id(Long productId);

}
