package com.mmhtoo.orderservice.service;

import com.mmhtoo.orderservice.dto.OrderCreateRequestDto;
import com.mmhtoo.orderservice.dto.OrderItemRequestDto;
import com.mmhtoo.orderservice.entity.OrderEntity;
import com.mmhtoo.orderservice.entity.OrderItemEntity;
import com.mmhtoo.shared.exception.custom.InvalidReferenceException;
import com.mmhtoo.shared.exception.custom.RequestFailException;

import java.util.List;
import java.util.Map;

public interface IOrderService {

  OrderEntity makeOrder(OrderCreateRequestDto dto) throws RequestFailException;

  Map<String,String> checkOrderItemStatus(List<OrderItemRequestDto> items) throws RequestFailException;

  Integer getTotalStockByProductId(String productId) throws RequestFailException;

  OrderEntity getOrderById(String id);

  void cancelOrder(String orderId) throws InvalidReferenceException, RequestFailException;

  void updateOrderStatus(String orderId, Integer statusId) throws InvalidReferenceException, RequestFailException;

  void onEndDeliveredOrder(String orderId) throws RequestFailException, InvalidReferenceException;

  void onAfterPickUpOrder(String orderId) throws RequestFailException, InvalidReferenceException;

  boolean isValidOrderType(Integer orderType);

  boolean isValidOrderState(Integer orderState);

  Map<String,Integer> getOrderStates();

  Map<String,Integer> getOrderTypes();

  void updateInventory(OrderItemEntity item) throws RequestFailException;

  void updateInventories(List<OrderItemEntity> items) throws RequestFailException;

}
