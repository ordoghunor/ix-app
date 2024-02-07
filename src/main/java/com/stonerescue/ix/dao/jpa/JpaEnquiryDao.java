package com.stonerescue.ix.dao.jpa;

import com.stonerescue.ix.dao.EnquiryDao;
import com.stonerescue.ix.model.Enquiry;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jpa")
public interface JpaEnquiryDao extends EnquiryDao, JpaRepository<Enquiry, Long> {
}
