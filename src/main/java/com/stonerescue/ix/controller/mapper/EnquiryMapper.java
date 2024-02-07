package com.stonerescue.ix.controller.mapper;

import com.stonerescue.ix.controller.dto.outgoing.EnquiryListingDto;
import com.stonerescue.ix.model.Enquiry;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface EnquiryMapper {
    EnquiryListingDto modelToListingDto(Enquiry enquiry);
    Collection<EnquiryListingDto> modelToListingDtos(Collection<Enquiry> enquiryCollection);
}
