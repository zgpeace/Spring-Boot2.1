package zgpeace.spring.starbucks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
public class StarbucksApplication implements ApplicationRunner {
  @Autowired
  private CoffeeRepository coffeeRepository;
  @Autowired
  private CoffeeService coffeeService;
  @Autowired
  private CoffeeOrderService orderService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("All Coffee: {}", coffeeRepository.findAll());

    Optional<Coffee> latte = coffeeService.findOneCoffee("Latte");
    if (latte.isPresent()) {
      CoffeeOrder order = orderService.createOrder("Li Lei", latte.get());
      log.info("Update INIT to PAID: {}", orderService.updateState(order, OrderState.PAID));
      log.info("Update PAID to INIT: {}", orderService.updateState(order, OrderState.INIT));
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(StarbucksApplication.class, args);
  }

}
