package com.zgpeace.rpc.provider.controller;

import com.zgpeace.rpc.common.Calculator;
import com.zgpeace.rpc.provider.service.CalculatorImpl;
import com.zgpeace.rpc.common.CalculateRPCRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ProviderController {
  private static Logger logger = LoggerFactory.getLogger(ProviderController.class);
  private Calculator calculator = new CalculatorImpl();

  public static void main(String[] args) throws IOException {
    logger.info("ProviderController running...");
    new ProviderController().run();
  }

  private void run() throws IOException {
    ServerSocket listener = new ServerSocket(9090);
    try {
      while (true) {
        Socket socket = listener.accept();
        try {
          // 将请求反序列化
          ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
          Object object = objectInputStream.readObject();

          logger.info("common is {}", object);
          System.out.println("common is {}" + object);

          //调用服务
          int result = 0;
          if (object instanceof CalculateRPCRequest) {
            CalculateRPCRequest calculateRPCRequest = (CalculateRPCRequest)object;
            logger.info("CalculateRPCRequest is {}", calculateRPCRequest.toString());
            if ("add".equals(calculateRPCRequest.getMethod())) {
              result = calculator.add(calculateRPCRequest.getA(), calculateRPCRequest.getB());
              logger.info("calculate success > {}", result);
            } else {
              throw new UnsupportedOperationException();
            }
          }

          // 返回结果
          ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
          objectOutputStream.writeObject(new Integer(result));
        } catch (Exception e) {
          logger.error("fail", e);
        } finally {
          socket.close();
        }
      }
    } finally {
      listener.close();
    }
  }

}
