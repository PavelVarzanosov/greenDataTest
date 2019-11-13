package com.greenData.bank.matchers;

import com.greenData.bank.modelsDTO.DepositDTO;
import org.mockito.ArgumentMatcher;

public class DepositDTOMatcher implements ArgumentMatcher<DepositDTO> {

    private DepositDTO left;

    public DepositDTOMatcher(DepositDTO updatedDeposit) {
        left = updatedDeposit;
    }

    @Override
    public boolean matches(DepositDTO right) {
        return left.getDepositId().equals(right.getDepositId()) &&
                left.getDateInMonth() == right.getDateInMonth() &&
                left.getOpeningDate().equals(right.getOpeningDate()) &&
                left.getBankId().equals(right.getBankId()) &&
                left.getClientId().equals(right.getClientId())&&
                left.getPercent() == right.getPercent();
    }
}
