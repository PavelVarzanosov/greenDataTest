package com.greenData.bank.matchers;

import com.greenData.bank.models.Bank;
import org.mockito.ArgumentMatcher;

public class BankMatcher implements ArgumentMatcher<Bank> {

    private Bank left;

    public BankMatcher(Bank updatedBank) {
        left = updatedBank;
    }

    @Override
    public boolean matches(Bank right) {
        return left.getBankId().equals(right.getBankId()) &&
                left.getBik() == right.getBik() &&
                left.getName().equals(right.getName());
    }
}
