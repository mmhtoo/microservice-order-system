package com.mmhtoo.inventoryservice.service;

import com.mmhtoo.inventoryservice.dto.InventoryCreateRequestDto;
import com.mmhtoo.inventoryservice.entity.ProductInventory;
import com.mmhtoo.shared.exception.custom.DuplicateEntityException;
import com.mmhtoo.shared.exception.custom.InvalidReferenceException;

import java.util.List;

public interface IProductInventoryService {

  ProductInventory createInventory(InventoryCreateRequestDto dto) throws DuplicateEntityException;

  ProductInventory updateInventory(InventoryCreateRequestDto dto, Integer id) throws DuplicateEntityException, InvalidReferenceException;

  Integer getStockByProductId(String productId);

  boolean isDuplicateInventory(String productId);

  boolean isDuplicateInventory(String productId, Integer id);

  ProductInventory getInventoryById(Integer id);

  ProductInventory getInventoryByProductId(String productId);

  List<ProductInventory> getAllInventories();

}
