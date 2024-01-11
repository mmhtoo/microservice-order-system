package com.mmhtoo.productservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ProductCategoryCreateRequestDto {

  @NotNull( message = "Category name is required!" )
  @NotEmpty( message = "Category name is required!" )
  @Length( min = 3 , max = 20 , message = "Category name must be between 3 and 20 characters!" )
  private String name;

}
