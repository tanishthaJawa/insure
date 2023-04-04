package com.insureMyTeam.insure.claims;

import com.insureMyTeam.insure.exceptions.ClaimNotFoundException;
import com.insureMyTeam.insure.exceptions.InsuranceNotFoundException;
import com.insureMyTeam.insure.policy.InsurancePolicy;
import com.insureMyTeam.insure.policy.InsuranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final ClaimMapper claimMapper;

    private final InsuranceRepository insuranceRepository;

    @Autowired
    public ClaimService(ClaimRepository claimRepository, ClaimMapper claimMapper, InsuranceRepository insuranceRepository) {
        this.claimRepository = claimRepository;
        this.claimMapper = claimMapper;
        this.insuranceRepository = insuranceRepository;
    }

    public ClaimDTO createClaim(ClaimDTO claimDTO, Long insuranceId) throws InsuranceNotFoundException {
        Claim claim = new Claim();
        claimMapper.mapDTOtoEntity(claimDTO, claim);
        InsurancePolicy insurancePolicy = insuranceRepository.findById(insuranceId).orElseThrow(InsuranceNotFoundException::new);
        claim.setPolicy(insurancePolicy);
        if (insurancePolicy.getClaims() == null) {
            insurancePolicy.setClaims(new ArrayList<>());
        }
        insurancePolicy.getClaims().add(claim);
        claimRepository.save(claim);
        return claimMapper.buildDTOFromEntity(claim);
    }

    public List<ClaimDTO> getAllClaims() {
        List<Claim> claimList = claimRepository.findAll();
        return claimList.stream().map(claimMapper::buildDTOFromEntity).collect(Collectors.toList());
    }

    public ClaimDTO getClaim(Long claimId) throws ClaimNotFoundException {
        Optional<Claim> claim = claimRepository.findById(claimId);
        if (claim.isEmpty()) {
            throw new ClaimNotFoundException();
        }
        return claimMapper.buildDTOFromEntity(claim.get());
    }

    public ClaimDTO updateClaim(Long claimId, ClaimDTO claimDTO) throws ClaimNotFoundException {
        Optional<Claim> claim = claimRepository.findById(claimId);
        if (claim.isEmpty()) {
            throw new ClaimNotFoundException();
        }
        claimMapper.mapDTOtoEntity(claimDTO, claim.get());
        claimRepository.save(claim.get());
        return claimMapper.buildDTOFromEntity(claim.get());
    }

    public String deleteClaim(Long claimId) throws ClaimNotFoundException {
        Optional<Claim> claim = claimRepository.findById(claimId);
        if (claim.isEmpty()) {
            throw new ClaimNotFoundException();
        }
        claimRepository.delete(claim.get());
        return "Claim of id : " + claimId + " deleted successfully";
    }
}
