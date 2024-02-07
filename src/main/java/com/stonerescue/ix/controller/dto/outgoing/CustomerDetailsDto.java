package com.stonerescue.ix.controller.dto.outgoing;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerDetailsDto {
    @NotNull
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String phoneNo;

}
