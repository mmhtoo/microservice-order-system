package com.mmhtoo.inventoryservice.repository;

import com.mmhtoo.inventoryservice.entity.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory,Integer> {

  Optional<ProductInventory> findByProductId(String productId);

  Integer countByProductId(String productId);


}
