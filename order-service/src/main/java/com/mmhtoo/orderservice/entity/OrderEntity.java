package com.mmhtoo.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table( name = "orders" )
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEntity {

  @Id
  @GeneratedValue( strategy = GenerationType.UUID )
  private String id;

  private String userId;

  private String address;

  private String remark;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private LocalDateTime deliveredAt;

  private Integer orderType;

  private Integer orderStatus;

  @Transient
  @JsonIgnore
  private List<OrderItemEntity> items;

}
