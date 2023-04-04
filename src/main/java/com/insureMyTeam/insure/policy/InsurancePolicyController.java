package com.insureMyTeam.insure.policy;

import com.insureMyTeam.insure.exceptions.ClientNotFoundException;
import com.insureMyTeam.insure.exceptions.InsuranceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@Slf4j
public class InsurancePolicyController {
    public InsurancePolicyController() {
        log.info("Insurance class created");
    }

    @Autowired
    InsuranceService insuranceService;

    @GetMapping
    public ResponseEntity<List<InsurancePolicyDTO>> getInsurances() {
        List<InsurancePolicyDTO> insurancePolicyDTOList = insuranceService.getALlInsurances();
        return new ResponseEntity<>(insurancePolicyDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsurancePolicyDTO> getInsuranceById(@PathVariable("id") Long insuranceId) throws InsuranceNotFoundException {
        InsurancePolicyDTO insurance = insuranceService.getInsurance(insuranceId);
        return new ResponseEntity<>(insurance, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<InsurancePolicyDTO> createInsurance(@RequestBody InsurancePolicyDTO insurancePolicyDTO, @RequestParam(required = false) Long clientId) throws ClientNotFoundException {
        InsurancePolicyDTO insurance = insuranceService.createInsurance(insurancePolicyDTO, clientId);
        return new ResponseEntity<>(insurance, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsurancePolicyDTO> updateInsurance(@PathVariable("id") Long insuranceId, @RequestBody InsurancePolicyDTO insurancePolicyDTO) throws InsuranceNotFoundException {
        InsurancePolicyDTO insurance = insuranceService.updateInsurance(insuranceId, insurancePolicyDTO);
        return new ResponseEntity<>(insurance, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInsurance(@PathVariable("id") Long insuranceId) throws InsuranceNotFoundException {
        String insuranceDeletionMessage = insuranceService.deleteInsurance(insuranceId);
        return new ResponseEntity<>(insuranceDeletionMessage, HttpStatus.OK);
    }
}
