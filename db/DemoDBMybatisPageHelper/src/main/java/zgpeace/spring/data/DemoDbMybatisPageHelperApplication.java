package zgpeace.spring.data;

import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zgpeace.spring.data.mapper.CoffeeMapper;
import zgpeace.spring.data.model.Coffee;

import java.util.List;

@SpringBootApplication
@Slf4j
@MapperScan("zgpeace.spring.data.mapper")
public class DemoDbMybatisPageHelperApplication implements ApplicationRunner {
  @Autowired
  private CoffeeMapper coffeeMapper;

  public static void main(String[] args) {
    SpringApplication.run(DemoDbMybatisPageHelperApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    coffeeMapper.findAllWithRowBounds(new RowBounds(1, 3))
        .forEach(c -> log.info("Page(1) Coffee {}", c));
    coffeeMapper.findAllWithRowBounds(new RowBounds(2, 3))
        .forEach(c -> log.info("Page(2) Coffee {}", c));

    log.info("===================");

    coffeeMapper.findAllWithRowBounds(new RowBounds(1, 0))
        .forEach(c -> log.info("Page(1) Coffee {}", c));

    log.info("===================");

    coffeeMapper.findAllWithParam(1, 3)
        .forEach(c -> log.info("Page(1) Coffee {}", c));
    List<Coffee> list = coffeeMapper.findAllWithParam(2, 3);
    PageInfo page = new PageInfo(list);
    log.info("PageInfo: {}", page);
  }
}
