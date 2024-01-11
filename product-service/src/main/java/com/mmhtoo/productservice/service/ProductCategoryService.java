package com.mmhtoo.productservice.service;

import com.mmhtoo.productservice.dto.ProductCategoryCreateRequestDto;
import com.mmhtoo.productservice.entity.ProductCategory;
import com.mmhtoo.productservice.exception.custom.DuplicateEntityException;
import com.mmhtoo.productservice.exception.custom.InvalidReferenceException;
import com.mmhtoo.productservice.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCategoryService implements IProductCategoryService {

  private final ProductCategoryRepository productCategoryRepository;

  @Override
  @Transactional
  public ProductCategory createCategory(ProductCategoryCreateRequestDto dto) throws DuplicateEntityException {
    log.info("===== createCategory method of ProductCategoryService is started ====");
    // check name is duplicate
    if(isNameDuplicate(dto.getName())){
      log.warn("===== Duplicate category name ====");
      throw new DuplicateEntityException(
          String.format("Category with %s is already existed!",dto.getName())
      );
    }
    log.info("===== createCategory method of ProductCategoryService is ended ====");
    return productCategoryRepository.save(mapToProductCategory(dto));
  }

  @Override
  public ProductCategory updateCategory(ProductCategoryCreateRequestDto dto, String id) throws DuplicateEntityException, InvalidReferenceException {
    log.info("===== updateCategory method os ProductCategoryService is started =====");
    // retrieve saved category with id
    ProductCategory savedCategory = getProductCategoryById(id);
    // will run if saved category with id is not present
    if(savedCategory == null){
      log.warn("===== Invalid category id to update category =====");
      throw new InvalidReferenceException(
         "Invalid category to update!"
      );
    }
    // check name is duplicate for updating
    if(isNameDuplicate(dto.getName(), id)){
      log.warn("===== Duplicate category name to update =====");
      throw new DuplicateEntityException(
          String.format("Product category with \"%s\" is already existed!", dto.getName())
      );
    }
    // setting new data and updated date
    savedCategory.setCategoryName(dto.getName());
    savedCategory.setUpdatedAt(LocalDateTime.now());
    log.info("===== Saving new updated category =====");
    return productCategoryRepository.save(savedCategory);
  }

  private ProductCategory mapToProductCategory(ProductCategoryCreateRequestDto dto){
    return ProductCategory.builder()
        .categoryName(dto.getName())
        .createdAt(LocalDateTime.now())
        .build();
  }

  @Override
  public boolean isNameDuplicate(String name) {
    return getProductCategoryByName(name) != null;
  }

  @Override
  public boolean isNameDuplicate(String name, String id) {
    ProductCategory savedCategory = getProductCategoryByName(name);
    return savedCategory != null && !savedCategory.getId().equals(id);
  }

  @Override
  public @Nullable ProductCategory getProductCategoryById(String id) {
    return productCategoryRepository.findById(id).orElse(null);
  }

  @Override
  public @Nullable ProductCategory getProductCategoryByName(String name) {
    return productCategoryRepository.findByCategoryName(name).orElse(null);
  }

  @Override
  public List<ProductCategory> getAllCategories() {
    return productCategoryRepository.findAll();
  }

}
