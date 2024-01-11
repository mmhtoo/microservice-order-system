package com.mmhtoo.orderservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponseDto {

  private String id;

  private String userId;

  private String address;

  private String remark;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private LocalDateTime deliveredAt;

  private String orderItems;

  private Integer orderType;

  private Integer orderState;;

}
