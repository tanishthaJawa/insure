package com.insureMyTeam.insure.client;

import com.insureMyTeam.insure.policy.InsurancePolicy;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {

    private Long id;
    private String name;
    private Date dateOfBirth;
    private String address;
    private String contactNumber;
    private List<InsurancePolicy> policyList;
}
