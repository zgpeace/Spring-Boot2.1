package com.zgpeace.rpc.client.controller;

import com.zgpeace.rpc.client.service.CalculatorRemoteImpl;
import com.zgpeace.rpc.common.Calculator;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

public class ClientController {
  private static Logger logger = LoggerFactory.getLogger(ClientController.class);

  public static void main(String[] args) {
    Calculator calculator = new CalculatorRemoteImpl();
    int result = calculator.add(1, 2);
    logger.info("result is {}", result);
  }
}
