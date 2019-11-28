package com.zgpeace.rpc.provider.service;

import com.zgpeace.rpc.common.Calculator;

public class CalculatorImpl implements Calculator {
  @Override
  public int add(int a, int b) {
    return a + b;
  }
}
