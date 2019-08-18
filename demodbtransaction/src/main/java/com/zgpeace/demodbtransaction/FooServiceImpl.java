package com.zgpeace.demodbtransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FooServiceImpl implements FooService {
  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private FooService fooService;

  @Override
  @Transactional
  public void insertRecord() {
    jdbcTemplate.execute("INSERT INTO FOO (BAR) VALUES ('AAA')");
  }

  @Override
  @Transactional(rollbackFor = RollbackException.class)
  public void insertThenRollback() throws RollbackException {
    jdbcTemplate.execute("INSERT INTO FOO (BAR) VALUES ('BBB')");
    throw new RollbackException();
  }

  @Override
  public void invokeInsertThenRollback() throws RollbackException {
    insertThenRollback();
  }


  @Override
  public void invokeInsertThenSuperRollback() throws RollbackException {
    fooService.insertThenRollback();
  }
}
