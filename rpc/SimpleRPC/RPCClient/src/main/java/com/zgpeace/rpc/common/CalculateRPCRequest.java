package com.zgpeace.rpc.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class CalculateRPCRequest implements Serializable {

  private String method;
  private int a;
  private int b;

  @Override
  public String toString() {
    return "CalculateRPCRequest{" +
            "method='" + method + '\'' +
            ", a=" + a +
            ", b=" + b +
            "}";
  }

}
