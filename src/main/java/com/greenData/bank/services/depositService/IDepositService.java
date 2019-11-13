package com.greenData.bank.services.depositService;

import com.greenData.bank.models.Deposit;
import com.greenData.bank.modelsDTO.DepositDTO;
import com.greenData.bank.modelsDTO.FilterDTO;
import javassist.NotFoundException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface IDepositService {

    Deposit saveDeposit(int percent, int dateInMonth, Date openingDate, UUID clientId, UUID bankId) throws NotFoundException;

    Deposit getDepositById(UUID id) throws NotFoundException;

    Deposit updateDeposit(DepositDTO depositDTO) throws Exception;

    void deleteById(UUID  id) throws NotFoundException;

    void deleteAll();

    List<Deposit> getDeposits(FilterDTO filterDTO);
}
