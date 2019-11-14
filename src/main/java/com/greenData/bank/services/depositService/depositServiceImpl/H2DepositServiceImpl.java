package com.greenData.bank.services.depositService.depositServiceImpl;

import com.greenData.bank.interfacesRepo.IBank;
import com.greenData.bank.interfacesRepo.IClient;
import com.greenData.bank.interfacesRepo.IDeposit;
import com.greenData.bank.models.Bank;
import com.greenData.bank.models.Client;
import com.greenData.bank.models.Deposit;
import com.greenData.bank.modelsDTO.DepositDTO;
import com.greenData.bank.modelsDTO.FilterCondition;
import com.greenData.bank.modelsDTO.FilterDTO;
import com.greenData.bank.services.depositService.IDepositService;
import com.greenData.bank.specification.abstractSpecification.Specification;
import com.greenData.bank.specification.abstractSpecification.SpecificationAbstractImpl;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Component("H2DepositImpl")
public class H2DepositServiceImpl implements IDepositService {

    @Autowired
    private IDeposit depositRep;
    @Autowired
    private IClient clientRep;
    @Autowired
    private IBank bankRep;

    public Deposit saveDeposit(int percent, int dateInMonth, Date openingDate, UUID clientId, UUID bankId) throws NotFoundException {
        Optional<Bank> optionalBank = this.bankRep.findById(bankId);
        Optional<Client> optionalClient = this.clientRep.findById(clientId);

        if (optionalBank.isPresent() && optionalClient.isPresent()) {
            Bank bank = optionalBank.get();
            Client client = optionalClient.get();
            Deposit deposit = new Deposit(percent, dateInMonth, openingDate, client, bank);
            return this.depositRep.save(deposit);
        }
        else {
            throw new NotFoundException("Client or bank not found");
        }
    }

    public Deposit getDepositById(UUID id) throws NotFoundException {
        Optional<Deposit> deposit = this.depositRep.findById(id);

        if(deposit.isPresent()){
            return deposit.get();
        }
        else {
            throw new NotFoundException("Deposit with id = " + id.toString() +" not found");
        }
    }

    public Deposit updateDeposit(DepositDTO depositDTO) throws NotFoundException {
        final Object lock = new Object();
        Optional<Deposit> optionalDeposit = this.depositRep.findById(depositDTO.getDepositId());
        Optional<Bank> optionalBank = this.bankRep.findById(depositDTO.getBankId());
        Optional<Client> optionalClient = this.clientRep.findById(depositDTO.getClientId());

        synchronized(lock) {
            if (optionalDeposit.isPresent() && optionalBank.isPresent() && optionalClient.isPresent()) {
                return this.depositRep.save(optionalDeposit.get());
            }
            else {
                throw new NotFoundException("Deposit, client or bank not found");
            }
        }
    }

    public void deleteById(UUID  id) throws NotFoundException {
        if (this.depositRep.findById(id).isPresent()) {
            depositRep.deleteById(id);
        }
        else {
            throw new NotFoundException("Deposit with id = " + id.toString() +" not found");
        }
    }

    public void deleteAll() {
        depositRep.deleteAll();
    }

    public List<Deposit> getDeposits(FilterDTO filterDTO) {
        Iterable<Deposit> iterableDeposit = depositRep.findAll();
        List<Deposit> depositList;
        List<Deposit> finalDepositList = new ArrayList<>();

        if(filterDTO.getFilterConditionList().size() > 0) {
            Specification<Deposit> finalCommonSpecification = getCommonSpecification(filterDTO);
            iterableDeposit.forEach((client) -> {
                try {
                    if(client.satisfies(finalCommonSpecification)) finalDepositList.add(client);
                } catch (Exception e) {
                    //TODO прокинуть exception выше
                }
            });
        } else {
            iterableDeposit.forEach(finalDepositList::add);
        }
        Comparator<Deposit> depositComparator = Comparator.<Deposit> naturalOrder();
        depositList = finalDepositList.stream()
                .sorted(depositComparator)
                .skip(filterDTO.getOffset())
                .limit(filterDTO.getLimit())
                .collect(Collectors.toList());
        if (filterDTO.getIsReverse()) depositList.sort(Collections.reverseOrder());
        return depositList;
    }

    private Specification<Deposit> getCommonSpecification(FilterDTO filterDTO) {
        FilterCondition filter = filterDTO.getFilterConditionList().get(0);
        Specification<Deposit> commonSpecification = new SpecificationAbstractImpl<Deposit>(filter.getFieldName(), filter.getOperator(), filter.getValueCondition());
        for (int i = 1; i< filterDTO.getFilterConditionList().size(); i++) {
            filter = filterDTO.getFilterConditionList().get(i);
            commonSpecification = commonSpecification.concatenate(filter.getConcatenateType(),
                    new SpecificationAbstractImpl<Deposit>(filter.getFieldName(), filter.getOperator(), filter.getValueCondition()));
        }
        return commonSpecification;
    }
}
