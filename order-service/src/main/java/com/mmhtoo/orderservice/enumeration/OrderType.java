package com.mmhtoo.orderservice.enumeration;

public enum OrderType {
  DELIVERY(0),
  PICK_UP(1);

  private final Integer val;

  OrderType(Integer val){
    this.val = val;
  }

  public Integer val(){
    return this.val;
  }

}
