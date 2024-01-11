package com.mmhtoo.orderservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UUID;

import java.util.List;

@Data
public class OrderCreateRequestDto {

  @NotNull( message = "User id is required!" )
  @NotEmpty( message = "User id is required!" )
  @UUID( message = "Invalid user id!" )
  private String userId;

  @Length( min = 0 , max = 200, message = "Remark must not be over 200 characters!" )
  private String remark;

  @Length( min = 0 , max = 200, message = "Address must not be over 200 characters!" )
  private String address;

  @NotNull( message = "Order type is required!" )
  private Integer orderType;

  private List<@Valid OrderItemRequestDto> items;

}
