package com.mmhtoo.productservice.dto;

public record InventoryCreateRequestDto(
    String productId,
    Integer stock
) {
}
