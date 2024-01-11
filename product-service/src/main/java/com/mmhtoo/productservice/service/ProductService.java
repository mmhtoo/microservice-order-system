package com.mmhtoo.productservice.service;

import com.mmhtoo.productservice.dto.InventoryCreateRequestDto;
import com.mmhtoo.productservice.dto.InventoryResponseDto;
import com.mmhtoo.productservice.dto.ProductCreateRequestDto;
import com.mmhtoo.productservice.entity.Product;
import com.mmhtoo.productservice.entity.ProductCategory;
import com.mmhtoo.productservice.exception.custom.DuplicateEntityException;
import com.mmhtoo.productservice.exception.custom.InvalidReferenceException;
import com.mmhtoo.productservice.repository.ProductRepository;
import com.mmhtoo.shared.dto.response.DataResponse;
import com.mmhtoo.shared.exception.custom.RequestFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements IProductService {

  private final ProductRepository productRepository;
  private final IProductCategoryService productCategoryService;
  private final RestClient inventoryServiceClient;

  @Override
  @Transactional
  public Product createProduct(ProductCreateRequestDto dto) throws DuplicateEntityException, InvalidReferenceException {
    log.info("===== createProduct() of ProductService started =====");
    // checking name is duplicate
    if(isNameDuplicate(dto.getName() , dto.getCategoryId())){
      log.warn("===== Duplicate Product Name Exception =====");
      throw new DuplicateEntityException(
          String.format("Product name with \"%s\" is already present for category \"%s\"!",dto.getName(), dto.getCategoryId())
      );
    }
    ProductCategory category = productCategoryService.getProductCategoryById(dto.getCategoryId());
    // checking category is present or not
    if(category == null){
      log.warn("===== Invalid category for product =====");
      throw new InvalidReferenceException(
        String.format("Category with \"%s\" id is invalid!",dto.getCategoryId())
      );
    }
    Product product = mapToProduct(dto);
    product.setCategory(category);
    return productRepository.save(product);
  }

  @Override
  public Product updateProduct(ProductCreateRequestDto dto, String productId) throws DuplicateEntityException, InvalidReferenceException {
    log.info("===== updateProduct() of ProductService started =====");
    // checking name is duplicate or not for related category
    if(isNameDuplicate(dto.getName(), dto.getCategoryId(), productId)){
      log.warn("===== Duplicate Product Name Exception =====");
      throw new DuplicateEntityException(
          String.format("Product name with \"%s\" is already present for category \"%s\"!",dto.getName(), dto.getCategoryId())
      );
    }
    ProductCategory category = productCategoryService.getProductCategoryById(dto.getCategoryId());
    // checking category is present or not
    if(category == null){
      log.warn("===== Invalid category for product =====");
      throw new InvalidReferenceException(
          String.format("Category with \"%s\" id is invalid!",dto.getCategoryId())
      );
    }
    Product savedProduct = getProductById(productId);
    // checking current product is present or not
    if(savedProduct == null){
      log.warn("===== Invalid product =====");
      throw new InvalidReferenceException(
          String.format("Product with \"%s\" is invalid!",productId)
      );
    }
    savedProduct.setName(dto.getName());
    savedProduct.setDescription(dto.getDescription());
    savedProduct.setUpdatedAt(LocalDateTime.now());
    savedProduct.setPrice(dto.getPrice());
    savedProduct.setCategory(category);
    return productRepository.save(savedProduct);
  }

  @Override
  @Transactional
  public void createInventory(InventoryCreateRequestDto dto) throws RequestFailException {
    DataResponse response  =  inventoryServiceClient.post()
         .body(dto)
         .retrieve()
         .toEntity(DataResponse.class)
         .getBody();

    if(response == null || !response.getStatus().equals(HttpStatus.CREATED.value())){
      throw new RequestFailException("Failed to create inventory!");
    }
  }

  private Product mapToProduct(ProductCreateRequestDto dto){
    log.info("===== mapToProduct() or ProductService started  =====");
    return Product.builder()
        .name(dto.getName())
        .description(dto.getDescription())
        .price(dto.getPrice())
        .createdAt(LocalDateTime.now())
        .build();
  }

  @Override
  public boolean isNameDuplicate(String name, String categoryId) {
    log.info("===== isNameDuplicate() of ProductService started =====");
    return productRepository
        .countByNameAndCategoryId(name,categoryId) > 0;
  }

  @Override
  public boolean isNameDuplicate(String name, String categoryId, String id) {
    log.info("===== isNameDuplicate() of ProductService started =====");
    return productRepository.countByNameAndCategoryId(name,categoryId) > 0 &&
        productRepository.findByIdAndNameAndCategoryId(id, name, categoryId).isEmpty();
  }

  @Override
  public Product getProductById(String id) {
    log.info("===== getProductById() of ProductService started =====");
    return productRepository.findById(id)
        .orElse(null);
  }

  @Override
  public @Nullable Product getProductByName(String name) {
    log.info("===== getProductByName() of ProductService started =====");
    return productRepository.findByName(name)
        .orElse(null);
  }

  @Override
  public List<Product> getProductsByCategoryId(String categoryId) {
    log.info("===== getProductsByCategoryId() of ProductService started =====");
    return productRepository.findByCategoryId(categoryId);
  }

  @Override
  public List<Product> getAllProducts() {
    log.info("===== getAllProducts() of ProductService started =====");
    return productRepository.findAll();
  }

}
