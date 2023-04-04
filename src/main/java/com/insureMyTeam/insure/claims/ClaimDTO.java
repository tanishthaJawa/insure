package com.insureMyTeam.insure.claims;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClaimDTO {
    private Long id;
    private Long claimNumber;
    private String description;
    private Date claimDate;
    private String claimStatus;
}
