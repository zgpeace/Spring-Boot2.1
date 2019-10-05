package zgpeace.spring.starbucks.repository;

import org.springframework.data.repository.CrudRepository;
import zgpeace.spring.starbucks.model.CoffeeCache;

import java.util.Optional;

public interface CoffeeCacheRepository extends CrudRepository<CoffeeCache, Long> {
  Optional<CoffeeCache> findOneByName(String name);
}
