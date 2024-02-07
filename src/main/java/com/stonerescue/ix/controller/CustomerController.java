package com.stonerescue.ix.controller;

import com.stonerescue.ix.controller.dto.incoming.CustomerIncomingDto;
import com.stonerescue.ix.controller.dto.outgoing.CustomerDetailsDto;
import com.stonerescue.ix.controller.dto.outgoing.CustomerListingDto;
import com.stonerescue.ix.controller.mapper.CustomerMapper;
import com.stonerescue.ix.dao.CustomerDao;
import com.stonerescue.ix.model.Customer;
import com.stonerescue.ix.service.CustomerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/customers")
public class CustomerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerService customerService;

    @GetMapping
    private Collection<CustomerListingDto> getCustomers() {
        LOGGER.info("Getting all customers.");

        return customerMapper.modelsToDtos(customerDao.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDetailsDto> getCustomer(@PathVariable("id") Long id) {
        LOGGER.info("Searching customer by id: {}", id.toString());

        boolean exists = customerDao.existsById(id);
        if (exists) {
            Customer customer = customerDao.getById(id);
            LOGGER.info("Customer with id={} exists, name is {}.", id, customer.getName());
            return ResponseEntity.ok(customerMapper.modelToDetailsDto(customer));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<CustomerListingDto>> searchCustomersByName(@RequestParam("name") String name) {
        LOGGER.info("Searching customer by name: {}", name);

        List<Customer> customers = customerService.findCustomersByName(name);
        if (customers.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<CustomerListingDto> customerDetailsDtoList = customers.stream()
                    .map(customerMapper::modelToListingDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(customerDetailsDtoList);
        }
    }

    @PostMapping
    public CustomerDetailsDto create(@RequestBody @Valid CustomerIncomingDto customerIncomingDto) {
        LOGGER.info("Creating new customer: {}", customerIncomingDto.toString());

        Customer customer = customerMapper.customerFromIncomingDto(customerIncomingDto);
        Customer savedCustomer = customerDao.saveAndFlush(customer);

        return customerMapper.modelToDetailsDto(savedCustomer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDetailsDto> update(@PathVariable("id") Long id, @RequestBody @Valid CustomerIncomingDto customerIncomingDto) {
        LOGGER.info("Modifying customer id={} with new properties: {}", id, customerIncomingDto.toString());
        boolean exists = customerDao.existsById(id);
        if (exists) {
            LOGGER.info("Customer with id={} exists. New values: {}", id, customerIncomingDto);
            Customer modifiedCustomer = customerMapper.customerFromIncomingDto(customerIncomingDto);
            modifiedCustomer.setId(id);
            return ResponseEntity.ok(customerMapper.modelToDetailsDto(customerDao.saveAndFlush(modifiedCustomer)));
        } else {
            LOGGER.info("Customer with id={} can not be modified, since it doesnt exist.", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Deleting customer with id={}", id);
        customerDao.delete(customerDao.getById(id));
    }

}
