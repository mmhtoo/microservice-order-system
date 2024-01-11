package com.mmhtoo.shared.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode( callSuper = true )
public class ErrorResponse extends CommonResponse {

  private Map<String,String> errors;

}
