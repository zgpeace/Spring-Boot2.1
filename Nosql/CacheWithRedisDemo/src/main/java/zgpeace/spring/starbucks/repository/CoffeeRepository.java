package zgpeace.spring.starbucks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zgpeace.spring.starbucks.model.Coffee;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
}
