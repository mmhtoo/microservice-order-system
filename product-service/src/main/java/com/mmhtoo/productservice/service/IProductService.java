package com.mmhtoo.productservice.service;

import com.mmhtoo.productservice.dto.InventoryCreateRequestDto;
import com.mmhtoo.productservice.dto.InventoryResponseDto;
import com.mmhtoo.productservice.dto.ProductCreateRequestDto;
import com.mmhtoo.productservice.entity.Product;
import com.mmhtoo.productservice.exception.custom.DuplicateEntityException;
import com.mmhtoo.productservice.exception.custom.InvalidReferenceException;
import com.mmhtoo.shared.exception.custom.RequestFailException;

import java.util.List;

public interface IProductService {

  Product createProduct(ProductCreateRequestDto dto) throws DuplicateEntityException, InvalidReferenceException;

  Product updateProduct(ProductCreateRequestDto dto, String productId) throws DuplicateEntityException, InvalidReferenceException;

  void createInventory(InventoryCreateRequestDto dto) throws RequestFailException;

  boolean isNameDuplicate(String name, String categoryId);

  boolean isNameDuplicate(String name, String categoryId, String id);

  Product getProductById(String id);

  Product getProductByName(String name);

  List<Product> getProductsByCategoryId(String categoryId);

  List<Product> getAllProducts();

}
