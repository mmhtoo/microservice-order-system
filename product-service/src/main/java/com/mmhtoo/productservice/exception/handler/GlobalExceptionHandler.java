package com.mmhtoo.productservice.exception.handler;

import com.mmhtoo.productservice.exception.custom.DuplicateEntityException;
import com.mmhtoo.productservice.exception.custom.InvalidReferenceException;
import com.mmhtoo.shared.dto.response.CommonResponse;
import com.mmhtoo.shared.exception.custom.RequestFailException;
import com.mmhtoo.shared.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({
      DuplicateEntityException.class,
      InvalidReferenceException.class,
      RequestFailException.class
  })
  public ResponseEntity<CommonResponse> handleCustomException(Exception e){
    Map<String,String> error = new HashMap<>();
    error.put("error",e.getMessage());
    return ResponseEntity.badRequest()
        .body(
            ResponseUtil.errorResponse(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                error
            )
        );
  }

}
