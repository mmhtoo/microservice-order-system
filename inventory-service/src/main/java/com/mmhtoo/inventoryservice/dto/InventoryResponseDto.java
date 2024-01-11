package com.mmhtoo.inventoryservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class InventoryResponseDto {

  private Integer id;

  private String productId;

  private Integer stock;

  private LocalDateTime createdAt;

  private LocalDateTime updateAt;

}
