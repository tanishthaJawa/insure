package com.insureMyTeam.insure.policy;

import com.insureMyTeam.insure.client.Client;
import com.insureMyTeam.insure.client.ClientRepository;
import com.insureMyTeam.insure.exceptions.ClientNotFoundException;
import com.insureMyTeam.insure.exceptions.InsuranceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InsuranceService {


    private final InsuranceRepository insuranceRepository;

    private final InsurancePolicyMapper insuranceMapper;

    private final ClientRepository clientRepository;

    @Autowired
    public InsuranceService(InsuranceRepository insuranceRepository, InsurancePolicyMapper insuranceMapper, ClientRepository clientRepository) {
        this.insuranceMapper = insuranceMapper;
        this.clientRepository = clientRepository;
        this.insuranceRepository = insuranceRepository;
    }

    public InsurancePolicyDTO createInsurance(InsurancePolicyDTO insurancePolicyDTO, Long clientId) throws ClientNotFoundException {
        InsurancePolicy insurancePolicy = new InsurancePolicy();
        insuranceMapper.mapDTOtoEntity(insurancePolicyDTO, insurancePolicy);

        if (clientId != null) {
            Client client = clientRepository.findById(clientId).orElseThrow(ClientNotFoundException::new);
            client.getPolicyList().add(insurancePolicy);
            insurancePolicy.setClient(client);
        }

        insuranceRepository.save(insurancePolicy);
        return insuranceMapper.buildDTOFromEntity(insurancePolicy);
    }

    public List<InsurancePolicyDTO> getALlInsurances() {
        List<InsurancePolicy> insurancePolicyList = insuranceRepository.findAll();
        return insurancePolicyList.stream().map(insuranceMapper::buildDTOFromEntity).collect(Collectors.toList());
    }

    public InsurancePolicyDTO getInsurance(Long insuranceId) throws InsuranceNotFoundException {
        Optional<InsurancePolicy> insurancePolicy = insuranceRepository.findById(insuranceId);
        if (insurancePolicy.isEmpty()) {
            throw new InsuranceNotFoundException();
        }
        return insuranceMapper.buildDTOFromEntity(insurancePolicy.get());
    }


    public InsurancePolicyDTO updateInsurance(Long insuranceId, InsurancePolicyDTO insurancePolicyDTO) throws InsuranceNotFoundException {
        Optional<InsurancePolicy> insurancePolicy = insuranceRepository.findById(insuranceId);
        if (insurancePolicy.isEmpty()) {
            throw new InsuranceNotFoundException();
        }
        insuranceMapper.mapDTOtoEntity(insurancePolicyDTO, insurancePolicy.get());
        insuranceRepository.save(insurancePolicy.get());
        return insuranceMapper.buildDTOFromEntity(insurancePolicy.get());
    }

    public String deleteInsurance(Long insuranceId) throws InsuranceNotFoundException {
        Optional<InsurancePolicy> insurancePolicy = insuranceRepository.findById(insuranceId);
        if (insurancePolicy.isEmpty()) {
            throw new InsuranceNotFoundException();
        }
        insuranceRepository.delete(insurancePolicy.get());
        return "InsurancePolicy of id : " + insuranceId + " deleted successfully";
    }

}
