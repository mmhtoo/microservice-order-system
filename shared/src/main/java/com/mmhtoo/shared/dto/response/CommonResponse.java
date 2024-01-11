package com.mmhtoo.shared.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommonResponse {

  private Integer status;

  private String responseDescription;

  private String timestamp;

}
