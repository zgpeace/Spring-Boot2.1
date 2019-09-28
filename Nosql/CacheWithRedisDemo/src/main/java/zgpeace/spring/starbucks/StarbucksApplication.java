package zgpeace.spring.starbucks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import zgpeace.spring.starbucks.model.Coffee;
import zgpeace.spring.starbucks.model.CoffeeOrder;
import zgpeace.spring.starbucks.model.OrderState;
import zgpeace.spring.starbucks.repository.CoffeeRepository;
import zgpeace.spring.starbucks.service.CoffeeOrderService;
import zgpeace.spring.starbucks.service.CoffeeService;

import java.util.Optional;

@Slf4j
@EnableTransactionManagement
@EnableJpaRepositories
@SpringBootApplication
@EnableCaching(proxyTargetClass = true)
public class StarbucksApplication implements ApplicationRunner {
  @Autowired
  private CoffeeService coffeeService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("Count: {}", coffeeService.findAllCoffee().size());
    for (int i = 0; i < 5; i++) {
      log.info("Reading from cache.");
      coffeeService.findAllCoffee();
    }
    Thread.sleep(5_000);
    log.info("Reading after refresh.");
    coffeeService.findAllCoffee().forEach(c -> log.info("Coffee {}", c.getName()));
  }

  public static void main(String[] args) {
    SpringApplication.run(StarbucksApplication.class, args);
  }

}
