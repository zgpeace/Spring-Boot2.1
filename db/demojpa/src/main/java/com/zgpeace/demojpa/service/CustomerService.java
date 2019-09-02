package com.zgpeace.demojpa.service;

import com.zgpeace.demojpa.bean.Customer;
import com.zgpeace.demojpa.dao.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

  @Autowired
  private CustomerRepository customerRepository;

  public List<Customer> getCustomers() {
    return (List<Customer>) customerRepository.findAll();
  }

  public List<Customer> getCustomersByPage(Pageable pageable) {
    return (List<Customer>) customerRepository.findAll(pageable).getContent();
  }

  public Customer getCustomerById(long id) {
    return customerRepository.findById(id).orElse(null);
  }

  public Customer addCustomer(Customer customer) {
    return customerRepository.save(customer);
  }

  public Customer updateCustomer(Customer customer) {
    return customerRepository.saveAndFlush(customer);
  }

  public void deleteCustomerById(long id) {
    customerRepository.deleteById(id);
  }

  public void deleteCustomerWithSqlByUserId(long id) {
    customerRepository.deleteCustomerWithSqlByUserId(id);
  }

}
