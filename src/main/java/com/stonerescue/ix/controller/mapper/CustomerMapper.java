package com.stonerescue.ix.controller.mapper;

import com.stonerescue.ix.controller.dto.incoming.CustomerIncomingDto;
import com.stonerescue.ix.controller.dto.outgoing.CustomerDetailsDto;
import com.stonerescue.ix.controller.dto.outgoing.CustomerListingDto;
import com.stonerescue.ix.model.Customer;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerListingDto modelToListingDto(Customer customer);
    CustomerDetailsDto modelToDetailsDto(Customer customer);
    Collection<CustomerListingDto> modelsToDtos(Collection<Customer> brands);
    Customer customerFromIncomingDto(CustomerIncomingDto customerIncomingDto);
}
