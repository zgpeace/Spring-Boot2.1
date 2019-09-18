package zgpeace.spring.data.mongo;

import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import zgpeace.spring.data.mongo.converter.MoneyReadConverter;
import zgpeace.spring.data.mongo.model.Coffee;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@SpringBootApplication
@Slf4j
public class MongoApplication implements ApplicationRunner {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Bean
  public MongoCustomConversions mongoCustomConversions() {
    return new MongoCustomConversions(Arrays.asList(new MoneyReadConverter()));
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    Coffee espresso = Coffee.builder()
        .name("espresso")
        .price(Money.of(CurrencyUnit.of("CNY"), 20.0))
        .createTime(new Date())
        .updateTime(new Date()).build();
    Coffee saved = mongoTemplate.save(espresso);
    log.info("Coffee {}", saved);

    List<Coffee> list = mongoTemplate.find(
        query(where("name").is("espresso")), Coffee.class
    );
    log.info("Find {} Coffee", list.size());
    list.forEach(c -> log.info("Coffee {}", c));

    Thread.sleep(1000); // sleep 1 second, to make the update time different from create time
    UpdateResult result = mongoTemplate.updateFirst(query(where("name").is("espresso")),
        new Update().set("price", Money.ofMajor(CurrencyUnit.of("CNY"), 30)).currentDate("updateTime"),
        Coffee.class);
    log.info("Update Result: {}", result.getModifiedCount());
    Coffee updateOne = mongoTemplate.findById(saved.getId(), Coffee.class);
    log.info("Update Result: {}", updateOne);

    mongoTemplate.remove(updateOne);
  }

  public static void main(String[] args) {
    SpringApplication.run(MongoApplication.class, args);
  }

}
