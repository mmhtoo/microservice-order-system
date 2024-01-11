package com.mmhtoo.productservice.service;

import com.mmhtoo.productservice.dto.ProductCategoryCreateRequestDto;
import com.mmhtoo.productservice.entity.ProductCategory;
import com.mmhtoo.productservice.exception.custom.DuplicateEntityException;
import com.mmhtoo.productservice.exception.custom.InvalidReferenceException;

import java.util.List;

public interface IProductCategoryService {

  ProductCategory createCategory(ProductCategoryCreateRequestDto dto) throws DuplicateEntityException;

  ProductCategory updateCategory(ProductCategoryCreateRequestDto dto, String id) throws DuplicateEntityException, InvalidReferenceException;

  boolean isNameDuplicate(String name);

  boolean isNameDuplicate(String name, String id);

  ProductCategory getProductCategoryById(String id);

  ProductCategory getProductCategoryByName(String name);

  List<ProductCategory> getAllCategories();

}
