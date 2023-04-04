package com.insureMyTeam.insure.serviceTests;

import com.insureMyTeam.insure.client.Client;
import com.insureMyTeam.insure.client.ClientRepository;
import com.insureMyTeam.insure.exceptions.ClaimNotFoundException;
import com.insureMyTeam.insure.exceptions.ClientNotFoundException;
import com.insureMyTeam.insure.exceptions.InsuranceNotFoundException;
import com.insureMyTeam.insure.policy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
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
public class InsuranceServiceTest {
    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private InsuranceRepository insuranceRepository;
    @MockBean
    private InsurancePolicyMapper insuranceMapper;

    private InsuranceService insuranceService;

    private InsurancePolicy policy;

    private InsurancePolicyDTO insuranceDTO;

    @BeforeEach
    public void setUp() {
        insuranceService = new InsuranceService(insuranceRepository,insuranceMapper,clientRepository );
        insuranceDTO =  InsurancePolicyDTO.builder()
                .policyNumber(123L)
                .premium(1200.0)
                .coverageAmount(200.0)
                .type("Health Insurance")
                .startDate(new Date(11122111))
                .endDate(new Date())
                .build();

        policy = new InsurancePolicy(1L,123L,"Health",100.0,200.0,new Date(),new Date());

    }


    @Test
    public void getAllInsurances() {
        List<InsurancePolicy> policies = List.of(policy);
        List<InsurancePolicyDTO> expectedInsurances = policies.stream().map(insurancePolicy -> insuranceMapper.buildDTOFromEntity(insurancePolicy)).collect(Collectors.toList());
        when(insuranceRepository.findAll()).thenReturn(policies);

        List<InsurancePolicyDTO> insurancesList = insuranceService.getALlInsurances();

        assertEquals(expectedInsurances, insurancesList);
        verify(insuranceRepository).findAll();

    }

    @Test
    public void shouldGetInsuranceIfFound() throws ClaimNotFoundException, InsuranceNotFoundException {
        when(insuranceRepository.findById(policy.getId())).thenReturn(Optional.of(policy));

        InsurancePolicyDTO expectedInsurance = insuranceService.getInsurance(1L);

        assertEquals(expectedInsurance, insuranceMapper.buildDTOFromEntity(policy));
        verify(insuranceRepository).findById(1L);

    }

    @Test
    public void shouldNotGetInsuranceAndThrowExceptionWhenInsuranceDoesNotExist() {
        when(insuranceRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(InsuranceNotFoundException.class, () -> insuranceService.getInsurance(policy.getId()));

    }

    @Test
    public void shouldDeleteInsuranceIfFound() throws InsuranceNotFoundException {
        when(insuranceRepository.findById(policy.getId())).thenReturn(Optional.of(policy));

        insuranceService.deleteInsurance(1L);

        verify(insuranceRepository).delete(policy);
    }

    @Test
    public void shouldNotDeleteAndThrowExceptionWhenInsuranceDoesNotExist()  {
        when(insuranceRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(InsuranceNotFoundException.class, () -> insuranceService.deleteInsurance(policy.getId()));
        verify(insuranceRepository, never()).delete(policy);
    }

    @Test
    public void shouldUpdateCInsuranceIfFound() throws  InsuranceNotFoundException {
        when(insuranceRepository.findById(policy.getId())).thenReturn(Optional.of(policy));

        insuranceService.updateInsurance(policy.getId(), insuranceDTO);

        verify(insuranceRepository).save(policy);
        verify(insuranceRepository).findById(policy.getId());
    }

    @Test
    public void shouldNotUpdateAndThrowExceptionWhenInsuranceDoesNotExist() {
        when(insuranceRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(InsuranceNotFoundException.class, () -> insuranceService.updateInsurance(policy.getId(), insuranceDTO));
        verify(insuranceRepository, never()).save(policy);
    }

    @Test
    public void shouldCreateInsuranceSuccessfully() throws  ClientNotFoundException {
        InsurancePolicyDTO policyDTO = InsurancePolicyDTO.builder()
                .policyNumber(policy.getPolicyNumber())
                .premium(policy.getPremium())
                .coverageAmount(policy.getCoverageAmount())
                .startDate(policy.getStartDate())
                .endDate(policy.getEndDate())
                .claims(policy.getClaims())
                .build();
        when(clientRepository.findById(1L)).thenReturn(Optional.of(new Client(1L, "Daisy Doe", new Date(2001111), "New York USA", "9877654112", new ArrayList<>())));

        insuranceService.createInsurance(policyDTO,1L );

        verify(insuranceRepository).save(any(InsurancePolicy.class));
    }

    @Test
    public void shouldNotCreateInsuranceAndThrowExceptionWhenClientNotFound() throws InsuranceNotFoundException {
        InsurancePolicyDTO policyDTO = InsurancePolicyDTO.builder()
                .policyNumber(policy.getPolicyNumber())
                .premium(policy.getPremium())
                .coverageAmount(policy.getCoverageAmount())
                .startDate(policy.getStartDate())
                .endDate(policy.getEndDate())
                .claims(policy.getClaims())
                .build();
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> insuranceService.createInsurance(policyDTO,1L ));
        verify(insuranceRepository,never()).save(any(InsurancePolicy.class));
    }
}




  

