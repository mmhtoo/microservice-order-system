package com.mmhtoo.orderservice.dto;

public record InventoryRequestDto (
    String productId,
    Integer stock
) {
}
