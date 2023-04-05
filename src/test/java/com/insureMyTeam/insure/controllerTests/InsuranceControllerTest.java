package com.insureMyTeam.insure.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insureMyTeam.insure.client.ClientDTO;
import com.insureMyTeam.insure.exceptions.InsuranceNotFoundException;
import com.insureMyTeam.insure.policy.InsurancePolicyController;
import com.insureMyTeam.insure.policy.InsurancePolicyDTO;
import com.insureMyTeam.insure.policy.InsuranceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InsurancePolicyController.class)
@RunWith(SpringRunner.class)
public class InsuranceControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    InsuranceService insuranceService;

    private InsurancePolicyDTO insurancePolicy;

    @BeforeEach
    public void init() {
        insurancePolicy = InsurancePolicyDTO.builder()
                .policyNumber(123L)
                .premium(1200.0)
                .coverageAmount(200.0)
                .type("Health Insurance")
                .startDate(new Date(11122111))
                .endDate(new Date())
                .build();
    }


    @Test
    @DisplayName("Should get all policies successfully")
    public void shouldGetAllInsurancePolicySuccessfully() throws Exception {
        List<InsurancePolicyDTO> insurancePolicies = new ArrayList<>(
                List.of(new InsurancePolicyDTO(1L, 123L, "health Insurance", 1000.0, 200.0, new Date(1098777), new Date(1224842482), new ArrayList<>()),
                        new InsurancePolicyDTO(2L, 124L, "life Insurance", 1200.0, 400.0, new Date(1009988), new Date(1222444545), new ArrayList<>())));

        when(insuranceService.getALlInsurances()).thenReturn(insurancePolicies);

        mockMvc.perform(get("/api/policies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].type").value("health Insurance"));
    }

    @Test
    @DisplayName("Should create policy successfully")
    public void shouldCreateInsurancePolicySuccessfully() throws Exception {
        when(insuranceService.createInsurance(insurancePolicy, 1L)).thenReturn(insurancePolicy);

        mockMvc.perform(post("/api/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(insurancePolicy)))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("Should get a policy successfully")
    public void shouldGetClientSuccessfully() throws Exception {
        when(insuranceService.getInsurance(1L)).thenReturn(insurancePolicy);

        mockMvc.perform(get("/api/policies/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.policyNumber").value(123L));
    }

    @Test
    @DisplayName("Should not get an insurance successfully")
    public void shouldNotGetInsuranceSuccessfully() throws Exception {
        doThrow(new InsuranceNotFoundException()).when(insuranceService).getInsurance(1L);

        mockMvc.perform(get("/api/policies/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Should update insurance successfully")
    public void shouldUpdateClientSuccessfully() throws Exception {
        when(insuranceService.updateInsurance(1L, insurancePolicy)).thenReturn(insurancePolicy);

        mockMvc.perform(put("/api/policies/1").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(insurancePolicy)))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    @DisplayName("Should delete insurance successfully")
    public void shouldDeleteInsuranceSuccessfully() throws Exception {
        when(insuranceService.deleteInsurance(1L)).thenReturn("InsurancePolicy of id : " + 1 + " deleted successfully");

        mockMvc.perform(delete("/api/policies/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print()).
                andExpect(result -> assertEquals("InsurancePolicy of id : " + 1 + " deleted successfully", result.getResponse().getContentAsString()));
    }

    @Test
    @DisplayName("Should throw policy not found exception and not delete policy if policy does not exist ")
    public void shouldNotDeletePolicyWhenPolicyDoesNotExist() throws Exception {
        doThrow(new InsuranceNotFoundException()).when(insuranceService).deleteInsurance(1L);

        mockMvc.perform(delete("/api/policies/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andDo(print());
    }
}
