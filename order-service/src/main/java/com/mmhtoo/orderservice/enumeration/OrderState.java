package com.mmhtoo.orderservice.enumeration;

public enum OrderState {
  INITED(0),
  VERIFIED(1),
  PREPARING(2),
  CANCELLED(-1),
  PREPARED(3),
  ON_WAY(4),
  COMPLETED(5);

  private final Integer value;

  OrderState(Integer value){
    this.value = value;
  }

  public Integer val(){
    return this.value;
  }

}
