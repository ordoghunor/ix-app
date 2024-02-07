package com.stonerescue.ix.service;

import com.stonerescue.ix.dao.CustomerDao;
import com.stonerescue.ix.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerDao customerDao;

    public List<Customer> findCustomersByName(String name) {
        return customerDao.findByNameContainingIgnoreCase(name);
    }
}
