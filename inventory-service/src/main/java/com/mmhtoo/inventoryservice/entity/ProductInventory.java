package com.mmhtoo.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table( name = "product_inventories" )
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductInventory {

  @Id
  @GeneratedValue( strategy = GenerationType.AUTO )
  private Integer id;

  private String productId;

  private Integer stock;

  private LocalDateTime createdAt;

  private  LocalDateTime updatedAt;

}
