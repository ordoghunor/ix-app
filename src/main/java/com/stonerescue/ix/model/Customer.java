package com.stonerescue.ix.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "customer")
public class Customer extends BaseEntity{
    @Column(nullable = false, length = 128)
    private String name;
    @Column(nullable = false, length = 128)
    private String email;
    @Column(nullable = false, length = 20)
    private String phoneNo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Enquiry> enquiries;
}
