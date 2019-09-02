package com.zgpeace.demodbjpa.repository;

import com.zgpeace.demodbjpa.model.Coffee;
import org.springframework.data.repository.CrudRepository;

public interface CoffeeRepository extends CrudRepository<Coffee, Long> {
}
