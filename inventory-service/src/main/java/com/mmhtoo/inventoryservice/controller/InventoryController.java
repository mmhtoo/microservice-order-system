package com.mmhtoo.inventoryservice.controller;

import com.mmhtoo.inventoryservice.dto.InventoryCreateRequestDto;
import com.mmhtoo.inventoryservice.dto.InventoryResponseDto;
import com.mmhtoo.inventoryservice.entity.ProductInventory;
import com.mmhtoo.inventoryservice.service.IProductInventoryService;
import com.mmhtoo.shared.dto.response.CommonResponse;
import com.mmhtoo.shared.exception.custom.DuplicateEntityException;
import com.mmhtoo.shared.exception.custom.InvalidReferenceException;
import com.mmhtoo.shared.util.BindingUtil;
import com.mmhtoo.shared.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( value = "/v1/inventories" )
@RequiredArgsConstructor
public class InventoryController {

  private final IProductInventoryService productInventoryService;

  @GetMapping( value = "/stock" )
  public ResponseEntity<CommonResponse> getInventoryByProductId(
      @RequestParam( value = "productId" ) String productId
  ){
    return ResponseEntity.ok()
        .body(
            ResponseUtil.dataResponse(
                "Success!",
                HttpStatus.OK.value(),
                productInventoryService.getStockByProductId(productId)
            )
        );
  }


  @PutMapping( value = "/stock" )
  public ResponseEntity<CommonResponse> updateStockByProductIdAndCategoryId(
      @Valid @RequestBody InventoryCreateRequestDto dto,
      BindingResult bindingResult,
      @RequestParam( value = "isIncrease" , required = false ) Boolean isIncrease
  ) throws DuplicateEntityException, InvalidReferenceException {
    if(bindingResult.hasErrors()){
      return ResponseEntity.badRequest()
          .body(
              ResponseUtil.errorResponse(
                  "Invalid request data!",
                  HttpStatus.BAD_REQUEST.value(),
                  BindingUtil.toErrorMap(bindingResult)
              )
          );
    }

    ProductInventory savedInventory = productInventoryService
        .getInventoryByProductId(dto.getProductId());

    if(savedInventory == null){
      throw new InvalidReferenceException("Invalid inventory to update!");
    }
    if(isIncrease == null){
      ProductInventory productInventory = productInventoryService.updateInventory(dto,savedInventory.getId());
      return ResponseEntity.ok()
          .body(
              ResponseUtil.dataResponse(
                  "Success!",
                  HttpStatus.OK.value(),
                  mapToInventoryResponse(productInventory)
              )
          );
    }

    if(dto.getStock() < 0){
      throw new InvalidReferenceException("Stock must not be minus!");
    }

    if(isIncrease){
      dto.setStock(savedInventory.getStock() + dto.getStock());
      ProductInventory productInventory = productInventoryService.updateInventory(dto,savedInventory.getId());
      return ResponseEntity.ok()
          .body(
              ResponseUtil.dataResponse(
                  "Success!",
                  HttpStatus.OK.value(),
                  mapToInventoryResponse(productInventory)
              )
          );
    }

    if(dto.getStock() > savedInventory.getStock()){
      throw new InvalidReferenceException("Un-sufficient stock");
    }

    dto.setStock(savedInventory.getStock() - dto.getStock());
    ProductInventory productInventory = productInventoryService.updateInventory(dto,savedInventory.getId());
    return ResponseEntity.ok()
        .body(
            ResponseUtil.dataResponse(
                "Success!",
                HttpStatus.OK.value(),
                mapToInventoryResponse(productInventory)
            )
        );
  }


  @GetMapping
  public ResponseEntity<CommonResponse> getAllInventories(){
    return ResponseEntity.ok()
        .body(
            ResponseUtil.dataResponse(
                "Success!",
                HttpStatus.OK.value(),
                productInventoryService.getAllInventories()
                    .stream().map(this::mapToInventoryResponse)
            )
        );
  }

  @PostMapping
  public ResponseEntity<CommonResponse> createInventory(
      @Valid @RequestBody InventoryCreateRequestDto dto ,
      BindingResult bindingResult
  ) throws DuplicateEntityException {
    if(bindingResult.hasErrors()){
      return ResponseEntity.badRequest()
          .body(
              ResponseUtil.errorResponse(
                  "Invalid request data!",
                  HttpStatus.BAD_REQUEST.value(),
                  BindingUtil.toErrorMap(bindingResult)
              )
          );
    }
    return ResponseEntity.ok()
        .body(
            ResponseUtil.dataResponse(
                "Success!",
                HttpStatus.CREATED.value(),
                mapToInventoryResponse(productInventoryService.createInventory(dto))
            )
        );
  }

  private @Nullable InventoryResponseDto mapToInventoryResponse(ProductInventory productInventory){
    if(productInventory == null) return null;
    return InventoryResponseDto.builder()
        .id(productInventory.getId())
        .createdAt(productInventory.getCreatedAt())
        .productId(productInventory.getProductId())
        .stock(productInventory.getStock())
        .updateAt(productInventory.getUpdatedAt())
        .build();
  }

}
