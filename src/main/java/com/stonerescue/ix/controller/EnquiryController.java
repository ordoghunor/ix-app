package com.stonerescue.ix.controller;

import com.stonerescue.ix.controller.dto.outgoing.EnquiryListingDto;
import com.stonerescue.ix.controller.mapper.EnquiryMapper;
import com.stonerescue.ix.dao.EnquiryDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/enquiry")
public class EnquiryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnquiryController.class);
    @Autowired
    private EnquiryDao enquiryDao;
    @Autowired
    private EnquiryMapper enquiryMapper;

    @GetMapping
    private Collection<EnquiryListingDto> getEnquiries() {
        return enquiryMapper.modelToListingDtos(enquiryDao.findAll());
    }
}
