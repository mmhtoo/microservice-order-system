package com.mmhtoo.productservice.repository;

import com.mmhtoo.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {

  Optional<Product> findByName(String name);

  List<Product> findByCategoryId(String categoryId);

  Integer countByNameAndCategoryId(String name, String categoryId);

  Optional<Product> findByNameAndCategoryId(String name, String categoryId);

  Optional<Product> findByIdAndNameAndCategoryId(String id, String name, String categoryId);

}
