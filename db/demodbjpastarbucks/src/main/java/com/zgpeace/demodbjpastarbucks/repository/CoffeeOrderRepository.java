package com.zgpeace.demodbjpastarbucks.repository;

import com.zgpeace.demodbjpastarbucks.model.CoffeeOrder;

import java.util.List;

public interface CoffeeOrderRepository extends BaseRepository<CoffeeOrder, Long> {
  List<CoffeeOrder> findByCustomerOrderById(String customer);

  List<CoffeeOrder> findByItems_Name(String name);
}
