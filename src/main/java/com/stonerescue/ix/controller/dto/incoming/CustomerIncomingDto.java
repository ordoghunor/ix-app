package com.stonerescue.ix.controller.dto.incoming;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CustomerIncomingDto {
    @NotNull
    @Length(max = 128)
    private String name;
    @NotNull
    @Length(max = 128)
    private String email;
    private String phoneNo;
}
