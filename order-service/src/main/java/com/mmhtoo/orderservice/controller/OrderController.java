package com.mmhtoo.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmhtoo.orderservice.dto.OrderCreateRequestDto;
import com.mmhtoo.orderservice.dto.OrderResponseDto;
import com.mmhtoo.orderservice.entity.OrderEntity;
import com.mmhtoo.orderservice.service.IOrderService;
import com.mmhtoo.shared.dto.response.CommonResponse;
import com.mmhtoo.shared.exception.custom.InvalidReferenceException;
import com.mmhtoo.shared.exception.custom.RequestFailException;
import com.mmhtoo.shared.util.BindingUtil;
import com.mmhtoo.shared.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping( value = "/v1/orders" )
@RequiredArgsConstructor
public class OrderController {

  private final IOrderService orderService;
  private final ObjectMapper objectMapper;

  @PostMapping
  public ResponseEntity<CommonResponse> makeOrder(
      @Valid @RequestBody OrderCreateRequestDto dto,
      BindingResult bindingResult
  ) throws RequestFailException, JsonProcessingException {
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
    Map<String,String> orderItemsStatus = orderService.checkOrderItemStatus(dto.getItems());
    if(!orderItemsStatus.isEmpty()){
      return ResponseEntity.unprocessableEntity()
            .body(
                ResponseUtil.errorResponse(
                    "Failed to order!",
                    HttpStatus.UNPROCESSABLE_ENTITY.value(),
                    orderItemsStatus
                )
            );

    }
    OrderEntity order = orderService.makeOrder(dto);
    return ResponseEntity.ok()
        .body(
            ResponseUtil.dataResponse(
                "Successfully ordered!",
                HttpStatus.OK.value(),
                mapToOrderResponse(order)
            )
        );
  }

  private OrderResponseDto mapToOrderResponse(OrderEntity order) throws JsonProcessingException {
    return OrderResponseDto.builder()
        .address(order.getAddress())
        .id(order.getId())
        .orderItems(objectMapper.writeValueAsString(order.getItems()))
        .deliveredAt(order.getDeliveredAt())
        .createdAt(order.getCreatedAt())
        .updatedAt(order.getUpdatedAt())
        .remark(order.getRemark())
        .orderState(order.getOrderStatus())
        .orderType(order.getOrderType())
        .build();
  }

  @GetMapping( value = "/types" )
  public ResponseEntity<CommonResponse> getOrderTypes(){
    return ResponseEntity.ok()
        .body(
            ResponseUtil.dataResponse(
                "Success!",
                HttpStatus.OK.value(),
                orderService.getOrderTypes()
            )
        );
  }

  @GetMapping( value = "/states" )
  public ResponseEntity<CommonResponse> getOrderStates(){
    return ResponseEntity.ok()
        .body(
            ResponseUtil.dataResponse(
                "Success!",
                HttpStatus.OK.value(),
                orderService.getOrderStates()
            )
        );
  }

  @PutMapping( value = "/cancel" )
  public ResponseEntity<CommonResponse> cancelOrder(
      @RequestParam( value = "orderId" ) String orderId
  ) throws RequestFailException, InvalidReferenceException {
    orderService.cancelOrder(orderId);
    return ResponseEntity.ok()
        .body(
            ResponseUtil.response(
                String.format("Successfully cancelled order with id %s .",orderId),
                HttpStatus.OK.value()
            )
        );
  }

  @PutMapping( value = "/delivered" )
  public ResponseEntity<CommonResponse> deliveredOrder(
      @RequestParam( value = "orderId" ) String orderId
  ) throws RequestFailException, InvalidReferenceException {
    orderService.onEndDeliveredOrder(orderId);
    return ResponseEntity.ok()
        .body(
            ResponseUtil.response(
                String.format("Successfully delivered order with id %s .",orderId),
                HttpStatus.OK.value()
            )
        );
  }

  @PutMapping( value = "/orders/{orderId}/status" )
  public ResponseEntity<CommonResponse> updateOrderStatus(
      @PathVariable( value = "orderId" ) String orderId ,
      @RequestParam( value = "status" ) Integer statusId
  ) throws RequestFailException, InvalidReferenceException {
    orderService.updateOrderStatus(orderId, statusId);
    return ResponseEntity.ok()
        .body(
            ResponseUtil.response(
                "Successfully updated order status!",
                HttpStatus.OK.value()
            )
        );
  }

  @GetMapping( value = "/orders/{orderId}" )
  public ResponseEntity<CommonResponse> getOrderDetail(
      @PathVariable( value = "orderId" ) String orderId
  ) throws JsonProcessingException {
   OrderEntity order = orderService.getOrderById(orderId);
   return ResponseEntity.ok()
       .body(
           ResponseUtil.dataResponse(
               order == null ? "No content!" : "Success!",
               HttpStatus.NO_CONTENT.value(),
               order == null ? null : mapToOrderResponse(order)
           )
       );
  }

}
