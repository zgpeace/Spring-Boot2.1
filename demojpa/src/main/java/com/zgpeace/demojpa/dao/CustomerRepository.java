package com.zgpeace.demojpa.dao;

import com.zgpeace.demojpa.bean.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

  List<Customer> findByLastName(String lastName);

  @Transactional(timeout = 10)
  @Modifying
  @Query("delete from Customer where id = ?1")
  void deleteCustomerWithSqlByUserId(Long id);

}
