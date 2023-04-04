package com.insureMyTeam.insure.client;

import com.insureMyTeam.insure.policy.InsurancePolicy;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "client")
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    @NotNull
    private String name;
    @Column
    @NotNull
    private Date dateOfBirth;
    @Column
    @NotNull
    private String address;
    @Column
    @NotNull
    private String contactNumber;

    @OneToMany(
            mappedBy = "client",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<InsurancePolicy> policyList;

    public Client() {
    }

    public Client(Long id, String name, Date dateOfBirth, String address, String contactNumber, List<InsurancePolicy> policyList) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.contactNumber = contactNumber;
        this.policyList = policyList;
    }
}
