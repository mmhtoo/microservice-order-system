package com.mmhtoo.shared.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode( callSuper = true )
public class DataResponse<T> extends CommonResponse {

  private T data;

}
