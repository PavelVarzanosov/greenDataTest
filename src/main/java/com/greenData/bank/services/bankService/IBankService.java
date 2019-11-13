package com.greenData.bank.services.bankService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.greenData.bank.models.Bank;
import com.greenData.bank.modelsDTO.FilterDTO;
import javassist.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface IBankService {

    Bank saveBank(String name, int bik);

    Bank getBankById(UUID id) throws NotFoundException;

    Bank updateBank(Bank bank) throws Exception;

    void deleteById(UUID  id) throws NotFoundException;

    void deleteAll();

    List<Bank> getBanks(FilterDTO filterDTO) throws JsonProcessingException;
}
