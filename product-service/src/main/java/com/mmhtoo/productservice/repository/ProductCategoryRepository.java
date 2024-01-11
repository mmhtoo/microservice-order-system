package com.mmhtoo.productservice.repository;

import com.mmhtoo.productservice.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory,String> {

  Optional<ProductCategory> findByCategoryName(String name);

}
