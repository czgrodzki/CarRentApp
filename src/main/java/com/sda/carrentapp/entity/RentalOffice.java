package com.sda.carrentapp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Data

@Entity(name = "rental_office")
public class RentalOffice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "office_name")
    private String officeName;
    private String webpage;
    @Column(name = "contact_address")
    private String contactAddress;
    private String owner;
    private String logo;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "rentalOffice")
    private List<Department> departments;

    public RentalOffice(String officeName, String webpage, String contactAddress, String owner, String logo) {
        this.officeName = officeName;
        this.webpage = webpage;
        this.contactAddress = contactAddress;
        this.owner = owner;
        this.logo = logo;
    }
}
