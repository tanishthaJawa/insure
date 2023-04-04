package com.insureMyTeam.insure.controllerTests;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.insureMyTeam.insure.claims.ClaimController;
import com.insureMyTeam.insure.claims.ClaimDTO;
import com.insureMyTeam.insure.claims.ClaimService;
import com.insureMyTeam.insure.exceptions.ClaimNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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

@WebMvcTest(ClaimController.class)
@RunWith(SpringRunner.class)
public class ClaimControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ClaimService claimService;


    @Test
    @DisplayName("Should get all claims successfully")
    public void shouldGetAllClaimsSuccessfully() throws Exception {
        List<ClaimDTO> claims = List.of(new ClaimDTO(1L, 123L, "investment", new Date(1098777), "accepted"),
                new ClaimDTO(2L, 124L, "life Insurance", new Date(1009988), "rejected"));

        when(claimService.getAllClaims()).thenReturn(claims);

        mockMvc.perform(get("/api/claims")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].description").value("investment"));
    }

    @Test
    @DisplayName("Should create claim successfully")
    public void shouldCreateClaimSuccessfully() throws Exception {
        ClaimDTO claim = ClaimDTO.builder()
                .claimNumber(123L)
                .claimStatus("Accepted")
                .description("Investment")
                .claimDate(new Date(11122111))
                .build();


        when(claimService.createClaim(claim, 1L)).thenReturn(claim);

        mockMvc.perform(post("/api/claims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(claim)))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("Should get a claim successfully")
    public void shouldGetClaimSuccessfully() throws Exception {
        ClaimDTO claim = ClaimDTO.builder()
                .claimNumber(123L)
                .claimStatus("Accepted")
                .description("Investment")
                .claimDate(new Date(11122111))
                .build();

        when(claimService.getClaim(1L)).thenReturn(claim);

        mockMvc.perform(get("/api/claims/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.claimNumber").value(123L));
    }

    @Test
    @DisplayName("Should not get an claim successfully")
    public void shouldNotGetClaimSuccessfully() throws Exception {

        doThrow(new ClaimNotFoundException()).when(claimService).getClaim(1L);

        mockMvc.perform(get("/api/claims/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Should update claim successfully")
    public void shouldUpdateClaimSuccessfully() throws Exception {
        ClaimDTO claim = ClaimDTO.builder()
                .claimNumber(123L)
                .claimStatus("Accepted")
                .description("Investment")
                .claimDate(new Date(11122111))
                .build();

        when(claimService.updateClaim(1L, claim)).thenReturn(claim);

        mockMvc.perform(put("/api/claims/1").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(claim)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Should delete claim successfully")
    public void shouldDeleteClaimSuccessfully() throws Exception {
        when(claimService.deleteClaim(1L)).thenReturn("Claim of id : " + 1 + " deleted successfully");

        mockMvc.perform(delete("/api/claims/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print()).
                andExpect(result -> assertEquals("Claim of id : " + 1 + " deleted successfully", result.getResponse().getContentAsString()));
    }

    @Test
    @DisplayName("Should throw claim not found exception and not delete claim if claim does not exist ")
    public void shouldNotDeleteClaimWhenClaimDoesNotExist() throws Exception {
        doThrow(new ClaimNotFoundException()).when(claimService).deleteClaim(1L);

        mockMvc.perform(delete("/api/claims/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andDo(print());
    }
}


