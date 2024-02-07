package com.stonerescue.ix.dao;

import com.stonerescue.ix.model.Customer;

import java.util.List;

public interface CustomerDao extends Dao<Customer>{
    List<Customer> findByNameContainingIgnoreCase(String name);
}
