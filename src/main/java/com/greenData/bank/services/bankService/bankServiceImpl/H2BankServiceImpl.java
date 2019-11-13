package com.greenData.bank.services.bankService.bankServiceImpl;

import com.greenData.bank.interfacesRepo.IBank;
import com.greenData.bank.models.Bank;
import com.greenData.bank.modelsDTO.FilterCondition;
import com.greenData.bank.modelsDTO.FilterDTO;
import com.greenData.bank.services.bankService.IBankService;
import com.greenData.bank.specification.abstractSpecification.Specification;
import com.greenData.bank.specification.abstractSpecification.SpecificationAbstractImpl;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Component("H2BankImpl")
public class H2BankServiceImpl implements IBankService {

    @Autowired
    private IBank bankRep;

    public Bank saveBank(String name, int bik) {
        Bank bank = new Bank(name, bik);
        this.bankRep.save(bank);
        return bank;
    }

    public Bank getBankById(UUID id) throws NotFoundException {
        Optional<Bank> bank = this.bankRep.findById(id);
        if(bank.isPresent()){
            return bank.get();
        }
        else {
            throw new NotFoundException("Bank with id = " + id.toString() +" not found");
        }
    }

    public Bank updateBank(Bank bank) throws NotFoundException {
        final Object lock = new Object();
        Optional<Bank> OptionalBank = this.bankRep.findById(bank.getBankId());
        synchronized(lock) {
            if (OptionalBank.isPresent()) {
                Bank savedBank = this.bankRep.save(bank);
                return savedBank;
            }
            else {
                throw new NotFoundException("Bank with id = " + bank.getBankId().toString() +" not found");
            }
        }
    }

    public void deleteById(UUID  id) throws NotFoundException {
        if (this.bankRep.existsById(id)) {
            bankRep.deleteById(id);
        }
        else {
            throw new NotFoundException("Bank with id = " + id.toString() +" not found");
        }
    }

    public void deleteAll() {
        bankRep.deleteAll();
    }

    public List<Bank> getBanks(FilterDTO filterDTO) {
        Iterable<Bank> iterableBank = bankRep.findAll();
        List<Bank> bankList;
        List<Bank> finalBankList = new ArrayList<>();

        if(filterDTO.getFilterConditionList().size() > 0) {
            Specification<Bank> finalCommonSpecification = getCommonSpecification(filterDTO);
            iterableBank.forEach((bank) -> {
                try {
                    if(bank.satisfies(finalCommonSpecification)) finalBankList.add(bank);
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            });
        } else {
            iterableBank.forEach((bank) -> {
                finalBankList.add(bank);
            });
        }
        Comparator<Bank> bankComparator = Comparator.<Bank> naturalOrder();
        bankList = finalBankList.stream()
                .sorted(bankComparator)
                .skip(filterDTO.getOffset())
                .limit(filterDTO.getLimit())
                .collect(Collectors.toList());
        if (filterDTO.getIsReverse()) {
            bankList.sort(Collections.reverseOrder());
        }
        return bankList;
    }

    private Specification<Bank> getCommonSpecification(FilterDTO filterDTO) {
        FilterCondition filter = filterDTO.getFilterConditionList().get(0);
        Specification<Bank> commonSpecification = new SpecificationAbstractImpl<Bank>(filter.getFieldName(), filter.getOperator(), filter.getValueCondition());
        for (int i = 1; i< filterDTO.getFilterConditionList().size(); i++) {
            filter = filterDTO.getFilterConditionList().get(i);
            commonSpecification = commonSpecification.concatenate(filter.getConcatenateType(),
                    new SpecificationAbstractImpl<Bank>(filter.getFieldName(), filter.getOperator(), filter.getValueCondition()));
        }
        return commonSpecification;
    }
}
