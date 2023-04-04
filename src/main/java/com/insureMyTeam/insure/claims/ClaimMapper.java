package com.insureMyTeam.insure.claims;

import org.springframework.stereotype.Component;

@Component
public class ClaimMapper {
    public void mapDTOtoEntity(ClaimDTO claimDTO, Claim claim) {
        claim.setClaimDate(claimDTO.getClaimDate());
        claim.setClaimStatus(claimDTO.getClaimStatus());
        claim.setDescription(claimDTO.getDescription());
        claim.setClaimNumber(claimDTO.getClaimNumber());
    }

    public ClaimDTO buildDTOFromEntity(Claim claim) {
        return ClaimDTO.builder().
                claimDate(claim.getClaimDate()).
                claimNumber(claim.getClaimNumber()).
                description(claim.getDescription())
                .id(claim.getId()).
                claimStatus(claim.getClaimStatus()).build();
    }
}
