package com.mmhtoo.productservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProductCategoryResponseDto {

  private String id;

  private String name;

  @JsonFormat( pattern = "yyyy-mm-dd hh:mm:ss" )
  private LocalDateTime createdAt;

  @JsonFormat( pattern = "yyyy-mm-dd hh:mm:ss" )
  private LocalDateTime updatedAt;

}
