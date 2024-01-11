package com.mmhtoo.productservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.UUID;

@Data
public class ProductCreateRequestDto {

  @NotNull( message = "Category name is required!" )
  @NotEmpty( message = "Category name is required!" )
  @Length( min = 3 , max = 30 , message = "Category name must be between 3 and 30 characters!")
  private String name;

  @Length(max = 200 , message = "Category description must not over 200 characters!")
  private String description;

  @NotNull( message = "Price is required!" )
  @Range( min = 0, message = "Product price must not be minus value!" )
  private Double price;

  @NotNull( message = "Product's stock is required!" )
  @Range( min = 0 , message = "Product's stock must not be minus value!")
  private Integer stock;

  @NotNull( message = "Category id is required!" )
  @NotEmpty( message = "Category id is required!" )
  @UUID( message = "Invalid category id format!" )
  private String categoryId;

}
