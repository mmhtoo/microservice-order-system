package com.mmhtoo.inventoryservice.service;

import com.mmhtoo.inventoryservice.dto.InventoryCreateRequestDto;
import com.mmhtoo.inventoryservice.entity.ProductInventory;
import com.mmhtoo.inventoryservice.repository.ProductInventoryRepository;
import com.mmhtoo.shared.exception.custom.DuplicateEntityException;
import com.mmhtoo.shared.exception.custom.InvalidReferenceException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductInventoryService implements IProductInventoryService {

  private final ProductInventoryRepository productInventoryRepository;

  @Override
  @Transactional
  public ProductInventory createInventory(InventoryCreateRequestDto dto) throws DuplicateEntityException {
    log.info("===== createInventory() of ProductInventoryService started =====");
    if(isDuplicateInventory(dto.getProductId())){
      log.warn("===== duplicate inventory ======");
      throw new DuplicateEntityException(
          String.format("Inventory is already present product %s and %s category!",dto.getProductId())
      );
    }
    ProductInventory productInventory = mapToProductInventory(dto);
    productInventory.setCreatedAt(LocalDateTime.now());
    return productInventoryRepository.save(productInventory);
  }

  @Override
  @Transactional
  public ProductInventory updateInventory(InventoryCreateRequestDto dto, Integer id) throws DuplicateEntityException, InvalidReferenceException {
    log.info("===== updateInventory() of ProductInventoryService started =====");
    ProductInventory savedInventory = getInventoryById(id);
    // check saved inventory is present or not
    if(savedInventory == null){
      log.warn("===== invalid inventory id ======");
      throw new InvalidReferenceException("Invalid inventory to update!");
    }
    // check duplicate
    if(isDuplicateInventory(dto.getProductId(), id)){
      log.warn("===== duplicate inventory ======");
      throw new DuplicateEntityException(
          String.format("Inventory is already present product %s!",dto.getProductId())
      );
    }
    savedInventory.setUpdatedAt(LocalDateTime.now());
    savedInventory.setStock(dto.getStock());
    return productInventoryRepository.save(savedInventory);
  }

  private ProductInventory mapToProductInventory(InventoryCreateRequestDto dto){
    return ProductInventory.builder()
        .productId(dto.getProductId())
        .stock(dto.getStock())
        .build();
  }

  @Override
  public Integer getStockByProductId(String productId) {
    ProductInventory productInventory = getInventoryByProductId(productId);
    return productInventory == null ? 0 : productInventory.getStock();
  }

  @Override
  public boolean isDuplicateInventory(String productId) {
    return productInventoryRepository
        .countByProductId(productId) > 0;
  }

  @Override
  public boolean isDuplicateInventory(String productId, Integer id) {
    ProductInventory savedInventory = getInventoryByProductId(productId);
    return savedInventory != null && !savedInventory.getId().equals(id);
  }

  @Override
  public @NotNull ProductInventory getInventoryById(Integer id) {
    return productInventoryRepository.findById(id)
        .orElse(null);
  }

  @Override
  public @NotNull ProductInventory getInventoryByProductId(
      String productId
  ) {
    return productInventoryRepository.findByProductId(productId)
        .orElse(null);
  }

  @Override
  public List<ProductInventory> getAllInventories() {
    return productInventoryRepository.findAll();
  }

}
