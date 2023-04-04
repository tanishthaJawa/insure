package com.insureMyTeam.insure.claims;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.insureMyTeam.insure.policy.InsurancePolicy;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "claim")
@Getter
@Setter
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    @NotNull
    private Long claimNumber;
    @Column
    @NotNull

    private String description;
    @Column
    @NotNull
    private Date claimDate;
    @Column
    @NotNull
    private String claimStatus;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "policy_id", nullable = false)
    @JsonIgnore
    private InsurancePolicy policy;

    public Claim(Long id, Long claimNumber, String description, Date claimDate, String claimStatus) {
        this.id = id;
        this.claimNumber = claimNumber;
        this.description = description;
        this.claimDate = claimDate;
        this.claimStatus = claimStatus;
    }

    public Claim() {
    }
}
