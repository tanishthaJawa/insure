package com.insureMyTeam.insure.policy;

import com.insureMyTeam.insure.claims.Claim;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InsurancePolicyDTO {

    private Long id;
    private Long policyNumber;
    private String type;
    private Double coverageAmount;
    private Double premium;
    private Date startDate;
    private Date endDate;

    private List<Claim> claims;
}

