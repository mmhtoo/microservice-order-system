package com.mmhtoo.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table( name = "product_categories" )
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductCategory {

  @Id
  @GeneratedValue( strategy = GenerationType.UUID )
  private String id;

  private String categoryName;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

}
