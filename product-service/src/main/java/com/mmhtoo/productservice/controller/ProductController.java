package com.mmhtoo.productservice.controller;

import com.mmhtoo.productservice.dto.InventoryCreateRequestDto;
import com.mmhtoo.productservice.dto.ProductCreateRequestDto;
import com.mmhtoo.productservice.dto.ProductResponseDto;
import com.mmhtoo.productservice.entity.Product;
import com.mmhtoo.productservice.exception.custom.DuplicateEntityException;
import com.mmhtoo.productservice.exception.custom.InvalidReferenceException;
import com.mmhtoo.productservice.service.IProductService;
import com.mmhtoo.shared.dto.response.CommonResponse;
import com.mmhtoo.shared.exception.custom.RequestFailException;
import com.mmhtoo.shared.util.BindingUtil;
import com.mmhtoo.shared.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( value = "/v1/products" )
@RequiredArgsConstructor
public class ProductController {

  private final IProductService productService;

  @GetMapping
  public ResponseEntity<CommonResponse> getAllProducts(
      @RequestParam( value = "categoryId" , required = false ) String categoryId
  ){

    List<ProductResponseDto> products = categoryId == null
        ? productService.getAllProducts()
          .stream()
          .map(this::mapToProductResponse)
          .toList()
        : productService.getProductsByCategoryId(categoryId)
          .stream()
          .map(this::mapToProductResponse)
          .toList();

    return ResponseEntity
        .ok()
        .body(
            ResponseUtil.dataResponse(
                "Success!",
                HttpStatus.OK.value(),
                products
            )
        );
  }

  private @Nullable ProductResponseDto mapToProductResponse(Product product){
    if(product == null) return null;
    return ProductResponseDto.builder()
        .categoryId(product.getCategory().getId())
        .id(product.getId())
        .categoryName(product.getCategory().getCategoryName())
        .price(product.getPrice())
        .createdAt(product.getCreatedAt())
        .updateAt(product.getUpdatedAt())
        .description(product.getDescription())
        .name(product.getName())
        .build();
  }

  @PostMapping
  public ResponseEntity<CommonResponse> createProduct(
      @Valid @RequestBody ProductCreateRequestDto dto,
      BindingResult bindingResult
  ) throws DuplicateEntityException, InvalidReferenceException, RequestFailException {
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
    Product savedProduct = productService.createProduct(dto);
    productService.createInventory(
        new InventoryCreateRequestDto(
            savedProduct.getId(),
            dto.getStock()
        )
    );

    ProductResponseDto response = mapToProductResponse(savedProduct);
    if(response != null){
      response.setStock(dto.getStock());
    }

    return ResponseEntity.ok()
        .body(
          ResponseUtil.dataResponse(
              "Successfully created!",
              HttpStatus.OK.value(),
              response
          )
        );
  }

  @GetMapping( value = "/{id}" )
  public ResponseEntity<CommonResponse> getProductById(
      @PathVariable String id
  ){
    Product product = productService.getProductById(id);
    return ResponseEntity.ok()
        .body(
            ResponseUtil.dataResponse(
                "Success!",
                product == null ? HttpStatus.NO_CONTENT.value() : HttpStatus.OK.value(),
                mapToProductResponse(product)
            )
        );
  }

  @PutMapping( value = "/{id}" )
  public ResponseEntity<CommonResponse> updateProduct(
      @Valid @RequestBody ProductCreateRequestDto dto,
      BindingResult bindingResult,
      @PathVariable String id
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
    Product updatedProduct = productService.updateProduct(dto,id);
    return ResponseEntity.ok()
        .body(
            ResponseUtil.dataResponse(
                "Successfully updated!",
                HttpStatus.OK.value(),
                mapToProductResponse(updatedProduct)
            )
        );
  }

}
