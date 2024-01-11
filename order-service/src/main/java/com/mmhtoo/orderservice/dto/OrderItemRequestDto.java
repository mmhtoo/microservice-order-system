package com.mmhtoo.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
public class OrderItemRequestDto {

  @NotNull( message = "Product id is required!" )
  @NotEmpty( message = "Product id is required!" )
  @UUID( message = "Invalid product id!" )
  private String productId;

  @NotNull( message = "Quantity is required!" )
  @Min( value = 1 , message = "Quantity must not be less than 1!" )
  private Integer quantity;

}
