package com.insureMyTeam.insure.claims;

import com.insureMyTeam.insure.exceptions.ClaimNotFoundException;
import com.insureMyTeam.insure.exceptions.InsuranceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
@Slf4j
public class ClaimController {

    public ClaimController() {
        log.info("Claim class created");
    }

    @Autowired
    ClaimService claimService;

    @GetMapping
    public ResponseEntity<List<ClaimDTO>> getClaims() {
        List<ClaimDTO> claimsList = claimService.getAllClaims();
        return new ResponseEntity<>(claimsList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimDTO> getClaimById(@PathVariable("id") Long claimId) throws ClaimNotFoundException {
        ClaimDTO claim = claimService.getClaim(claimId);
        return new ResponseEntity<>(claim, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ClaimDTO> createClaim(@RequestBody ClaimDTO claimDTO, @RequestParam(required = false) Long insuranceId) throws InsuranceNotFoundException {
        ClaimDTO claim = claimService.createClaim(claimDTO, insuranceId);
        return new ResponseEntity<>(claim, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClaimDTO> updateClaim(@PathVariable("id") Long claimId, @RequestBody ClaimDTO ClaimDTO) throws ClaimNotFoundException {
        ClaimDTO claim = claimService.updateClaim(claimId, ClaimDTO);
        return new ResponseEntity<>(claim, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClaim(@PathVariable("id") Long claimId) throws ClaimNotFoundException {
        String claimDeletionMessage = claimService.deleteClaim(claimId);
        return new ResponseEntity<>(claimDeletionMessage, HttpStatus.OK);
    }

}
