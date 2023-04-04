package com.insureMyTeam.insure.policy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.insureMyTeam.insure.claims.Claim;
import com.insureMyTeam.insure.client.Client;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "policy")
@Getter
@Setter
public class InsurancePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    @NotNull
    private Long policyNumber;
    @Column
    @NotNull
    private String type;
    @Column
    @NotNull
    private Double coverageAmount;
    @Column
    @NotNull
    private Double premium;
    @Column
    @NotNull
    private Date startDate;
    @Column
    @NotNull
    private Date endDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonIgnore
    private Client client;

    @OneToMany(
            mappedBy = "policy",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Claim> claims;

    public InsurancePolicy(Long id, Long policyNumber, String type, Double coverageAmount, Double premium, Date startDate, Date endDate) {
        this.id = id;
        this.policyNumber = policyNumber;
        this.type = type;
        this.coverageAmount = coverageAmount;
        this.premium = premium;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public InsurancePolicy() {
    }
}
