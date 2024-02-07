package com.stonerescue.ix.dao.jpa;

import com.stonerescue.ix.dao.CustomerDao;
import com.stonerescue.ix.model.Customer;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("jpa")
public interface JpaCustomerDao extends CustomerDao, JpaRepository<Customer, Long> {
}
