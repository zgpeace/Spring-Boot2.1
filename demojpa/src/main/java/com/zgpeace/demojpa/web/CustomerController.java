package com.zgpeace.demojpa.web;

import com.zgpeace.demojpa.bean.Customer;
import com.zgpeace.demojpa.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Customer getCustomerById(@PathVariable("id") long id) {
        return customerService.getCustomerById(id);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String addCustomer(@RequestParam(value = "firstName", required = true) String firstName,
                              @RequestParam(value = "lastName", required = true) String lastName) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        Customer result = customerService.addCustomer(customer);
        return result.toString();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String updateCustomer(@PathVariable("id") long id, @RequestParam(value = "firstName", required = true) String firstName,
                                 @RequestParam(value = "lastName", required = true) String lastName) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        Customer result = customerService.updateCustomer(customer);

        return result.toString();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String deleteCustomerById(@PathVariable("id") long id) {
        customerService.deleteCustomerById(id);
        return "finish delete, Please check whether is success";
    }
}
































