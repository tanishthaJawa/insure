package com.insureMyTeam.insure.serviceTests;

import com.insureMyTeam.insure.claims.*;
import com.insureMyTeam.insure.exceptions.ClaimNotFoundException;
import com.insureMyTeam.insure.exceptions.InsuranceNotFoundException;
import com.insureMyTeam.insure.policy.InsurancePolicy;
import com.insureMyTeam.insure.policy.InsuranceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class ClaimServiceTest {
    @MockBean
    private ClaimRepository claimRepository;
    @MockBean
    private InsuranceRepository insuranceRepository;
    @MockBean
    private ClaimMapper claimMapper;

    private ClaimService claimService;

    private Claim claim;

    private ClaimDTO claimDTO;

    @BeforeEach
    public void setUp() {
        claimService = new ClaimService(claimRepository, claimMapper, insuranceRepository);
        claimDTO = ClaimDTO.builder()
                .claimNumber(123L)
                .claimStatus("Accepted")
                .description("Investment")
                .claimDate(new Date(11122111))
                .build();

        claim = new Claim(1L, 123L, "Investment", new Date(2001111), "Accepted");

    }


    @Test
    public void getAllClaims() {
        List<Claim> claims = List.of(claim);
        List<ClaimDTO> expectedClaims = claims.stream().map(claim -> claimMapper.buildDTOFromEntity(claim)).collect(Collectors.toList());
        when(claimRepository.findAll()).thenReturn(claims);

        List<ClaimDTO> claimList = claimService.getAllClaims();

        assertEquals(expectedClaims, claimList);
        verify(claimRepository).findAll();

    }

    @Test
    public void shouldGetClaimIfFound() throws ClaimNotFoundException {
        when(claimRepository.findById(claim.getId())).thenReturn(Optional.of(claim));

        ClaimDTO expectedClaim = claimService.getClaim(1L);
        assertEquals(expectedClaim, claimMapper.buildDTOFromEntity(claim));
        verify(claimRepository).findById(1L);

    }

    @Test
    public void shouldNotGetClaimAndThrowExceptionWhenClaimDoesNotExist() {
        when(claimRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ClaimNotFoundException.class, () -> claimService.getClaim(claim.getId()));

    }

    @Test
    public void shouldDeleteClaimIfFound() throws ClaimNotFoundException {
        when(claimRepository.findById(claim.getId())).thenReturn(Optional.of(claim));

        claimService.deleteClaim(1L);

        verify(claimRepository).delete(claim);
    }

    @Test
    public void shouldNotDeleteAndThrowExceptionWhenClaimDoesNotExist()  {
        when(claimRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ClaimNotFoundException.class, () -> claimService.deleteClaim(claim.getId()));
        verify(claimRepository, never()).delete(claim);
    }

    @Test
    public void shouldUpdateClaimIfFound() throws ClaimNotFoundException {
        when(claimRepository.findById(claim.getId())).thenReturn(Optional.of(claim));
        claimService.updateClaim(claim.getId(), claimDTO);

        verify(claimRepository).save(claim);
        verify(claimRepository).findById(claim.getId());
    }

    @Test
    public void shouldNotUpdateAndThrowExceptionWhenClaimDoesNotExist() {
        when(claimRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ClaimNotFoundException.class, () -> claimService.updateClaim(claim.getId(), claimDTO));
        verify(claimRepository, never()).save(claim);
    }

    @Test
    public void shouldCreateClaimSuccessfully() throws InsuranceNotFoundException {
        ClaimDTO claimDTO1 = ClaimDTO.builder()
                .claimNumber(claim.getClaimNumber())
                .claimStatus(claim.getClaimStatus())
                .description(claim.getDescription())
                .claimDate(claim.getClaimDate())
                .build();
        when(insuranceRepository.findById(1L)).thenReturn(Optional.of(new InsurancePolicy(1L,123L,"Health",100.0,200.0,new Date(),new Date())));
        claimService.createClaim(claimDTO1,1L );

        verify(claimRepository).save(any(Claim.class));
    }

    @Test
    public void shouldNotCreateClaimAndThrowExceptionWhenInsuranceNotFound()  {
        ClaimDTO claimDTO1 = ClaimDTO.builder()
                .claimNumber(claim.getClaimNumber())
                .claimStatus(claim.getClaimStatus())
                .description(claim.getDescription())
                .claimDate(claim.getClaimDate())
                .build();
        when(insuranceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InsuranceNotFoundException.class, () -> claimService.createClaim(claimDTO1,1L ));
        verify(claimRepository,never()).save(any(Claim.class));
    }
}
