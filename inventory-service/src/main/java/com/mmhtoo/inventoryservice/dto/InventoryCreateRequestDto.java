package com.mmhtoo.inventoryservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
@Builder
public class InventoryCreateRequestDto {

  @NotNull( message = "Product Id is required!" )
  @NotEmpty( message = "Product Id must not be blank!" )
  @UUID( message = "Invalid product Id!" )
  private String productId;

  @NotNull( message = "Stock is required!" )
  @Min( value = 0 , message = "Stock must not be minus!" )
  private Integer stock;

}
