package com.mmhtoo.productservice.dto;

public record InventoryResponseDto(
    String productId,

    String categoryId,

    Integer stock,

    Integer id,

    String createdAt,

    String updatedAt
) {
}
