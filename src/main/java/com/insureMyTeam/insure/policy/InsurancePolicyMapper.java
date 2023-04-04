package com.insureMyTeam.insure.policy;

import org.springframework.stereotype.Component;

@Component
public class InsurancePolicyMapper {
    public void mapDTOtoEntity(InsurancePolicyDTO insurancePolicyDTO, InsurancePolicy policy) {
        policy.setPolicyNumber(insurancePolicyDTO.getPolicyNumber());
        policy.setPremium(insurancePolicyDTO.getPremium());
        policy.setType(insurancePolicyDTO.getType());
        policy.setEndDate(insurancePolicyDTO.getEndDate());
        policy.setStartDate(insurancePolicyDTO.getStartDate());
        policy.setCoverageAmount(insurancePolicyDTO.getCoverageAmount());
    }

    public InsurancePolicyDTO buildDTOFromEntity(InsurancePolicy policy) {
        return InsurancePolicyDTO.builder().
                policyNumber(policy.getPolicyNumber()).
                startDate(policy.getStartDate()).
                endDate(policy.getEndDate()).
                type(policy.getType()).
                coverageAmount(policy.getCoverageAmount()).
                premium(policy.getPremium()).
                claims(policy.getClaims()).
                id(policy.getId()).
                build();
    }
}
