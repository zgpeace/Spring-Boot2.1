package zgpeace.spring.data.mongorepository.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import zgpeace.spring.data.mongorepository.model.Coffee;

import java.util.List;

public interface CoffeeRepository extends MongoRepository<Coffee, String> {
  List<Coffee> findByName(String name);
}
