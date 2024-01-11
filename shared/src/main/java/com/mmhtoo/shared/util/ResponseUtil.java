package com.mmhtoo.shared.util;

import com.mmhtoo.shared.dto.response.CommonResponse;
import com.mmhtoo.shared.dto.response.DataResponse;
import com.mmhtoo.shared.dto.response.ErrorResponse;

import java.time.LocalDateTime;
import java.util.Map;

public class ResponseUtil {

  public static CommonResponse response(
      String message,
      Integer status
  ){
    CommonResponse commonResponse = new CommonResponse();
    commonResponse.setResponseDescription(message);
    commonResponse.setStatus(status);
    commonResponse.setTimestamp(LocalDateTime.now().toString());
    return commonResponse;
  }

  public static CommonResponse errorResponse(
    String message,
    Integer status,
    Map<String,String> errors
  ){
    ErrorResponse response = new ErrorResponse();
    response.setTimestamp(LocalDateTime.now().toString());
    response.setResponseDescription(message);
    response.setStatus(status);
    response.setErrors(errors);
    return response;
  }

  public static <T> CommonResponse dataResponse(
      String message,
      Integer status,
      T data
  ){
    DataResponse<T> response = new DataResponse<>();
    response.setResponseDescription(message);
    response.setData(data);
    response.setTimestamp(LocalDateTime.now().toString());
    response.setStatus(status);
    return response;
  }

}
