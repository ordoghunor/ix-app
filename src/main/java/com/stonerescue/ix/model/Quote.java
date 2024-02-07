package com.stonerescue.ix.model;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "quote")
public class Quote extends BaseEntity{
    @Column(scale = 2)
    private Double amount;
    @Column(nullable = false, length = 4000)
    private String quoteDetails;
    @OneToOne
    @JoinColumn(name = "enquiry_id", referencedColumnName = "id")
    private Enquiry enquiry;
}
