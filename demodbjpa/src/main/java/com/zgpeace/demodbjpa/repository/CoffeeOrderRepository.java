package com.zgpeace.demodbjpa.repository;

import com.zgpeace.demodbjpa.model.CoffeeOrder;
import org.springframework.data.repository.CrudRepository;

public interface CoffeeOrderRepository extends CrudRepository<CoffeeOrder, Long> {
}
