package com.zgpeace.demojpa.web;

import com.zgpeace.demojpa.bean.Customer;
import com.zgpeace.demojpa.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @RequestMapping(value = "/listPageable", method = RequestMethod.GET)
    public List<Customer> getCustomersByPage() {
        int page = 0, size =3;
        Sort sort = new Sort(Sort.Direction.DESC, "lastName");
        Pageable pageable = PageRequest.of(page, size, sort);
        return customerService.getCustomersByPage(pageable);
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


    @RequestMapping(value = "/sql/{id}", method = RequestMethod.DELETE)
    public String deleteCustomerWithSqlById(@PathVariable("id") long id) {
        customerService.deleteCustomerWithSqlByUserId(id);
        return "finish sql delete, Please check whether is success";
    }
}
































