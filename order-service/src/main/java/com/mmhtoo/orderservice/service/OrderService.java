package com.mmhtoo.orderservice.service;

import com.mmhtoo.orderservice.dto.InventoryRequestDto;
import com.mmhtoo.orderservice.dto.OrderCreateRequestDto;
import com.mmhtoo.orderservice.dto.OrderItemRequestDto;
import com.mmhtoo.orderservice.entity.OrderEntity;
import com.mmhtoo.orderservice.entity.OrderItemEntity;
import com.mmhtoo.orderservice.enumeration.OrderState;
import com.mmhtoo.orderservice.enumeration.OrderType;
import com.mmhtoo.orderservice.repository.OrderItemRepository;
import com.mmhtoo.orderservice.repository.OrderRepository;
import com.mmhtoo.shared.dto.response.DataResponse;
import com.mmhtoo.shared.exception.custom.InvalidReferenceException;
import com.mmhtoo.shared.exception.custom.RequestFailException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

  private final OrderRepository orderRepository;
  private final RestClient inventoryServiceClient;
  private final OrderItemRepository orderItemRepository;

  @Override
  @Transactional
  public OrderEntity makeOrder(OrderCreateRequestDto dto) throws RequestFailException {
    OrderEntity order = mapToOrderEntity(dto);
    order.setOrderStatus(OrderState.INITED.val());
    order = orderRepository.save(order);

    OrderEntity finalOrder = order;

    List<OrderItemEntity> items = dto.getItems()
        .stream()
        .map( item -> OrderItemEntity.builder()
            .productId(item.getProductId())
            .quantity(item.getQuantity())
            .order(finalOrder)
            .build()).toList();

    finalOrder.setItems(orderItemRepository.saveAll(items));
    updateInventories(items);
    return finalOrder;
  }

  private OrderEntity mapToOrderEntity(OrderCreateRequestDto dto){
    return OrderEntity.builder()
        .orderType(dto.getOrderType())
        .address(dto.getAddress())
        .remark(dto.getRemark())
        .build();
  }

  @Override
  public Map<String, String> checkOrderItemStatus(List<OrderItemRequestDto> items) throws RequestFailException {
    Map<String,String> statusForProducts = new HashMap<>();
    if(items == null || items.isEmpty()){
      statusForProducts.put("error","Order item must include to make order!");
      return statusForProducts;
    }
    for(OrderItemRequestDto item : items){
      Integer totalStock = getTotalStockByProductId(item.getProductId());
      if(item.getQuantity() > totalStock){
        statusForProducts.put(
            item.getProductId(),
            String.format("Product %s is available only maximum %s item!",item.getProductId(),totalStock)
        );
      }
    }
    return statusForProducts;
  }

  @Override
  public Integer getTotalStockByProductId(String productId) throws RequestFailException {
    DataResponse response = inventoryServiceClient.get()
        .uri("/stock?productId={productId}",productId)
        .retrieve()
        .toEntity(DataResponse.class)
        .getBody();

    if(response == null){
      throw new RequestFailException(
          String.format("Failed to retrieve stock for product with id %s !",productId)
      );
    }
    return (Integer) response.getData();
   }

  @Override
  public OrderEntity getOrderById(String id) {
    return orderRepository.findById(id)
        .orElse(null);
  }

  @Override
  @Transactional
  public void cancelOrder(String orderId) throws InvalidReferenceException, RequestFailException {
    OrderEntity order = getOrderById(orderId);
    if(order == null){
      throw new InvalidReferenceException("Invalid order to cancel!");
    }

    if(order.getDeliveredAt() != null){
      throw new RequestFailException("Unable to cancel order that has been delivered!");
    }
    order.setOrderStatus(OrderState.CANCELLED.val());
    order.setUpdatedAt(LocalDateTime.now());
    orderRepository.save(order);
  }

  @Override
  public void updateOrderStatus(String orderId, Integer statusId) throws InvalidReferenceException, RequestFailException {
    OrderEntity order = getOrderById(orderId);
    if(order == null){
      throw new InvalidReferenceException("Invalid order to cancel!");
    }
    if(order.getOrderStatus().equals(OrderState.CANCELLED.val()) || order.getOrderStatus().equals(OrderState.COMPLETED.val())){
      throw new RequestFailException("Unable to update order that is in cancelled or completed state!");
    }
    if(isValidOrderState(statusId)){
      throw new InvalidReferenceException("Invalid order status to update!");
    }
    order.setOrderStatus(statusId);
    orderRepository.save(order);
  }

  @Override
  public void onEndDeliveredOrder(String orderId) throws RequestFailException, InvalidReferenceException {
    updateOrderStatus(orderId,OrderState.COMPLETED.val());
  }

  @Override
  public void onAfterPickUpOrder(String orderId) throws RequestFailException, InvalidReferenceException {
    updateOrderStatus(orderId, OrderState.COMPLETED.val());
  }

  @Override
  public boolean isValidOrderType(Integer orderType) {
    return Arrays.stream(OrderType
        .values())
        .toList()
        .stream().map(OrderType::val)
        .toList()
        .contains(orderType);
  }

  @Override
  public boolean isValidOrderState(Integer orderState) {
    return Arrays.stream(OrderState.values())
        .toList()
        .stream().map(OrderState::val)
        .toList()
        .contains(orderState);
  }

  @Override
  public Map<String, Integer> getOrderStates() {
    return Arrays.stream(OrderState.values()).toList()
        .stream().collect(Collectors.toMap(OrderState::name,OrderState::val));
  }

  @Override
  public Map<String, Integer> getOrderTypes() {
    return Arrays.stream(OrderType.values()).toList()
        .stream().collect(Collectors.toMap(OrderType::name,OrderType::val));
  }

  @Override
  public void updateInventory(OrderItemEntity item) throws RequestFailException {
    DataResponse response = inventoryServiceClient.put()
        .uri("/stock?isIncrease=false")
        .body(new InventoryRequestDto(item.getProductId(),item.getQuantity()))
        .retrieve()
        .toEntity(DataResponse.class)
        .getBody();
    if(response == null || !response.getStatus().equals(HttpStatus.OK.value())){
      throw new RequestFailException(
          response == null
              ? "Failed to update!"
              :response.getResponseDescription()
      );
    }
  }

  @Override
  public void updateInventories(List<OrderItemEntity> items) throws RequestFailException {
    for(OrderItemEntity item : items){
      updateInventory(item);
    }
  }

}
