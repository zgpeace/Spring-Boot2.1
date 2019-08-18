package com.zgpeace.demodberrorcode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemodberrorcodeApplicationTests {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test(expected = CustomDuplicatedKeyException.class)
  public void testThrowingCustomException() {
    jdbcTemplate.execute("INSERT INTO FOO (ID, BAR) VALUES (1, 'A')");
    jdbcTemplate.execute("INSERT INTO FOO (ID, BAR) VALUES (1, 'B')");
  }

  @Test
  public void testThrowingException() {
    jdbcTemplate.execute("INSERT INTO FOO (ID, BAR) VALUES (1, 'A')");
    jdbcTemplate.execute("INSERT INTO FOO (ID, BAR) VALUES (1, 'B')");
  }

}
