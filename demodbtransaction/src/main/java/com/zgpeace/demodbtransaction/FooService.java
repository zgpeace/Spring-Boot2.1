package com.zgpeace.demodbtransaction;

public interface FooService {
  void insertRecord();

  void insertThenRollback() throws RollbackException;

  void invokeInsertThenRollback() throws RollbackException;
}
