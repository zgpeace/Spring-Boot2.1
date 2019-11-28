package com.zgpeace.rpc.client.service;

import com.zgpeace.rpc.common.Calculator;
import com.zgpeace.rpc.common.CalculateRPCRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class CalculatorRemoteImpl implements Calculator {
  public static final int PORT = 9090;
  private static Logger logger = LoggerFactory.getLogger(CalculatorRemoteImpl.class);

  @Override
  public int add(int a, int b) {
    List<String> addressList = lookupProvider("Calculator.add");
    String address = chooseTarget(addressList);
    try {
      Socket socket = new Socket(address, PORT);

      // 将请求序列化
      CalculateRPCRequest calculateRPCRequest = generateRqeust(a, b);
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

      // 将请求发给服务提供方
      objectOutputStream.writeObject(calculateRPCRequest);

      // 将响应体反序列化
      ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
      Object response = objectInputStream.readObject();

      logger.info("response is {}", response);
      if (response instanceof Integer) {
        return (Integer)response;
      } else {
        throw new InternalError();
      }

    } catch (Exception e) {
      logger.error("fail", e);
      throw new InternalError();
    }
  }

  private CalculateRPCRequest generateRqeust(int a, int b) {
    CalculateRPCRequest calculateRPCRequest = new CalculateRPCRequest();
    calculateRPCRequest.setA(a);
    calculateRPCRequest.setB(b);
    calculateRPCRequest.setMethod("add");
    return calculateRPCRequest;
  }

  private String chooseTarget(List<String> providers) {
    if (null == providers || providers.size() == 0) {
      throw new IllegalArgumentException();
    }
    return providers.get(0);
  }

  private List<String> lookupProvider(String name) {
    List<String> strings = new ArrayList<>();
    strings.add("127.0.0.1");
    return strings;
  }
}

