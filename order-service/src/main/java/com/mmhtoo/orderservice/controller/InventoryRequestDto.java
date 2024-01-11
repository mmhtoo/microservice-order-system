package com.mmhtoo.orderservice.controller;

public record InventoryRequestDto (
    String productId,
    Integer stock
) {
}
