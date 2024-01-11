package com.mmhtoo.orderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table( name = "order_items" )
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemEntity {

  @Id
  @GeneratedValue( strategy = GenerationType.AUTO )
  private Integer id;

  private String productId;

  private Integer quantity;

  @ManyToOne
  private OrderEntity order;

}
