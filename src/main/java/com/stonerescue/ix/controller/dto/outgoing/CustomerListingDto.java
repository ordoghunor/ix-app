package com.stonerescue.ix.controller.dto.outgoing;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerListingDto {
    @NotNull
    private Long id;
    @NotNull
    private String name;
    private String email;
}
