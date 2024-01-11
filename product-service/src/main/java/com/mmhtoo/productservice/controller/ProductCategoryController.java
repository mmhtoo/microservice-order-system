package com.mmhtoo.productservice.controller;

import com.mmhtoo.productservice.dto.ProductCategoryCreateRequestDto;
import com.mmhtoo.productservice.dto.ProductCategoryResponseDto;
import com.mmhtoo.productservice.entity.ProductCategory;
import com.mmhtoo.productservice.exception.custom.DuplicateEntityException;
import com.mmhtoo.productservice.exception.custom.InvalidReferenceException;
import com.mmhtoo.productservice.service.IProductCategoryService;
import com.mmhtoo.shared.dto.response.CommonResponse;
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
@RequestMapping( value = "/v1/product-categories" )
@RequiredArgsConstructor
public class ProductCategoryController {

  private final IProductCategoryService productCategoryService;

  @GetMapping
  public ResponseEntity<CommonResponse> getAllCategories(){
    return ResponseEntity.ok()
        .body(
            ResponseUtil.dataResponse(
                "Success!",
                HttpStatus.OK.value(),
                productCategoryService.getAllCategories()
                    .stream()
                    .map(this::mapToProductCategoryResponse)
            )
        );
  }

  private @Nullable ProductCategoryResponseDto mapToProductCategoryResponse(ProductCategory productCategory){
    if(productCategory == null) return null;
    return ProductCategoryResponseDto.builder()
        .id(productCategory.getId())
        .name(productCategory.getCategoryName())
        .createdAt(productCategory.getCreatedAt())
        .updatedAt(productCategory.getUpdatedAt())
        .build();
  }

  @PostMapping
  public ResponseEntity<CommonResponse> createCategory(
      @Valid @RequestBody ProductCategoryCreateRequestDto dto,
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
               "Successfully created!",
               HttpStatus.CREATED.value(),
               mapToProductCategoryResponse(
                   productCategoryService.createCategory(dto)
               )
           )
       );
  }

  @GetMapping( value = "{id}" )
  public ResponseEntity<CommonResponse> getCategoryById(
      @PathVariable String id
  ){
    ProductCategory savedCategory = productCategoryService
        .getProductCategoryById(id);
    return ResponseEntity.ok()
        .body(
            ResponseUtil.dataResponse(
                "Success!",
                savedCategory == null ? HttpStatus.NO_CONTENT.value() : HttpStatus.OK.value(),
                mapToProductCategoryResponse(savedCategory)
            )
        );
  }

  @PutMapping( value = "{id}" )
  public ResponseEntity<CommonResponse> updateCategory(
      @Valid @RequestBody ProductCategoryCreateRequestDto dto,
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
    ProductCategory updatedCategory = productCategoryService.updateCategory(dto,id);
    return ResponseEntity.ok()
        .body(
            ResponseUtil.dataResponse(
                "Successfully updated!",
                HttpStatus.OK.value(),
                mapToProductCategoryResponse(updatedCategory)
            )
        );
  }

}
