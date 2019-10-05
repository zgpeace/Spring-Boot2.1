package zgpeace.spring.starbucks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zgpeace.spring.starbucks.model.CoffeeOrder;

public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder, Long> {
}
