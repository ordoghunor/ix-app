package com.stonerescue.ix.dao.jpa;

import com.stonerescue.ix.dao.QuoteDao;
import com.stonerescue.ix.model.Quote;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jpa")
public interface JpaQuoteDao extends QuoteDao, JpaRepository<Quote, Long> {
}
