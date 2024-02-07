package com.stonerescue.ix.model;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "enquiry")
public class Enquiry extends BaseEntity{
    @Column(nullable = false, scale = 2)
    private Double m2;
    @Column(nullable = false, length = 25)
    private String postCode;
    @Column(nullable = false, length = 4000)
    private String enquiryDetails;
    @OneToOne(mappedBy = "enquiry", cascade = CascadeType.ALL)
    private Quote quote;
    @ManyToOne
    @JoinColumn(name="customer_id", referencedColumnName = "id")
    private Customer customer;
}
