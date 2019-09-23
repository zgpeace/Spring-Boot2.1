package zgpeace.spring.starbucks;

import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import zgpeace.spring.starbucks.model.Coffee;
import zgpeace.spring.starbucks.model.CoffeeOrder;
import zgpeace.spring.starbucks.model.OrderState;
import zgpeace.spring.starbucks.repository.CoffeeRepository;
import zgpeace.spring.starbucks.service.CoffeeOrderService;
import zgpeace.spring.starbucks.service.CoffeeService;

import java.util.Map;
import java.util.Optional;

@Slf4j
@EnableTransactionManagement
@EnableJpaRepositories
@SpringBootApplication
public class StarbucksApplication implements ApplicationRunner {
  @Autowired
  private CoffeeService coffeeService;
  @Autowired
  private JedisPool jedisPool;
  @Autowired
  private JedisPoolConfig jedisPoolConfig;

  @Bean
  @ConfigurationProperties("redis")
  public JedisPoolConfig jedisPoolConfig() {
    return new JedisPoolConfig();
  }

  @Bean(destroyMethod = "close")
  public JedisPool jedisPool(@Value("localhost") String host) {
    return new JedisPool(jedisPoolConfig(), host);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info(jedisPoolConfig.toString());

    try (Jedis jedis = jedisPool.getResource()) {
      coffeeService.findAllCoffee().forEach(c -> {
        jedis.hset("starbucks-menu",
            c.getName(),
            Long.toString(c.getPrice().getAmountMinorLong()));
      });

      Map<String, String> menu = jedis.hgetAll("starbucks-menu");
      log.info("Menu: {}", menu);

      String price = jedis.hget("starbucks-menu", "espresso");
      log.info("espresso - {}",
          Money.ofMinor(CurrencyUnit.of("CNY"), Long.parseLong(price)));

    }
  }

  public static void main(String[] args) {
    SpringApplication.run(StarbucksApplication.class, args);
  }

}
