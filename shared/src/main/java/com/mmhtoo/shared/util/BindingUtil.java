package com.mmhtoo.shared.util;

import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

public class BindingUtil {

  public static Map<String,String> toErrorMap(BindingResult bindingResult){
    Map<String,String> error = new HashMap<>();
    bindingResult.getFieldErrors()
        .forEach(fieldError -> {
          error.put(fieldError.getField(),fieldError.getDefaultMessage());
        });
    return error;
  }

}
