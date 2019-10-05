package zgpeace.spring.starbucks;

import io.lettuce.core.ReadFrom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import zgpeace.spring.starbucks.converter.BytesToMoneyConverter;
import zgpeace.spring.starbucks.converter.MoneyToBytesConverter;
import zgpeace.spring.starbucks.model.Coffee;
import zgpeace.spring.starbucks.model.CoffeeOrder;
import zgpeace.spring.starbucks.model.OrderState;
import zgpeace.spring.starbucks.repository.CoffeeRepository;
import zgpeace.spring.starbucks.service.CoffeeOrderService;
import zgpeace.spring.starbucks.service.CoffeeService;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@EnableTransactionManagement
@EnableJpaRepositories
@EnableRedisRepositories
@SpringBootApplication
public class StarbucksApplication implements ApplicationRunner {
  @Autowired
  private CoffeeService coffeeService;

  @Bean
  public LettuceClientConfigurationBuilderCustomizer customizer() {
    return builder -> builder.readFrom(ReadFrom.MASTER_PREFERRED);
  }

  @Bean
  public RedisCustomConversions redisCustomConversions() {
    return  new RedisCustomConversions(
        Arrays.asList(new MoneyToBytesConverter(), new BytesToMoneyConverter())
    );
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    Optional<Coffee> c = coffeeService.findSimpleCoffeeFromCache("mocha");
    log.info("Coffee: {}", c);

    for (int i = 0; i < 5; i++) {
      c = coffeeService.findSimpleCoffeeFromCache("mocha");
    }

    log.info("Value from Redis: {}", c);
  }

  public static void main(String[] args) {
    SpringApplication.run(StarbucksApplication.class, args);
  }

}
