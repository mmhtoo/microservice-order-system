package com.mmhtoo.productservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProductResponseDto {

  private String id;

  private String name;

  private String description;

  @JsonFormat( pattern = "yyyy-mm-dd hh:mm:ss" )
  private LocalDateTime createdAt;

  private Double price;

  private Integer stock;

  @JsonFormat( pattern = "yyyy-mm-dd hh:mm:ss" )
  private LocalDateTime updateAt;

  private String categoryId;

  private String categoryName;

}
