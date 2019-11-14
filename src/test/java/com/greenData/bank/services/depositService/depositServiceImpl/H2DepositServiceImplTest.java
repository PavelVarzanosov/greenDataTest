package com.greenData.bank.services.depositService.depositServiceImpl;

import com.greenData.bank.models.Bank;
import com.greenData.bank.models.Client;
import com.greenData.bank.models.Deposit;
import com.greenData.bank.models.enums.LegalForms;
import com.greenData.bank.modelsDTO.DepositDTO;
import com.greenData.bank.modelsDTO.FilterCondition;
import com.greenData.bank.modelsDTO.FilterDTO;
import com.greenData.bank.services.bankService.IBankService;
import com.greenData.bank.services.clientService.IClientService;
import com.greenData.bank.services.depositService.IDepositService;
import com.greenData.bank.specification.enums.ConcatenateType;
import com.greenData.bank.specification.enums.Operator;
import javassist.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
class H2DepositServiceImplTest {

    @Autowired
    @Qualifier("H2DepositImpl")
    IDepositService depositService;

    @Autowired
    @Qualifier("H2ClientImpl")
    IClientService clientService;

    @Autowired
    @Qualifier("H2BankImpl")
    IBankService bankService;

    private Deposit deposit;
    private Deposit deposit1;
    private Deposit deposit2;
    private Deposit deposit3;
    private Client client;
    private Bank bank;

    @BeforeEach
    void setUp() throws NotFoundException {
        MockitoAnnotations.initMocks(this);
        client = clientService.saveClient("clientName", "shortClientName", "address", LegalForms.LTD);
        bank = bankService.saveBank("bankName", 4);
        deposit = depositService.saveDeposit(5, 12, new Date(), client.getClientId(), bank.getBankId());
        deposit1 = depositService.saveDeposit(10, 6, new Date(), client.getClientId(), bank.getBankId());
        deposit2 = depositService.saveDeposit(15, 12, new Date(), client.getClientId(), bank.getBankId());
        deposit3 = depositService.saveDeposit(20, 3, new Date(), client.getClientId(), bank.getBankId());
    }

    @AfterEach
    void tearDown() {
        depositService.deleteAll();
        clientService.deleteAll();
        bankService.deleteAll();
    }

    @Test
    void testSaveDepositWithException(){
       try {
           Deposit newDeposit = depositService.saveDeposit(10, 12, new Date(), UUID.randomUUID(), bank.getBankId());
       }
        catch (Exception exception) {
            assertEquals("Client or bank not found", exception.getMessage());
        }
    }

    @Test
    void testGetDepositById() throws NotFoundException {

        Deposit gettedDeposit = depositService.getDepositById(deposit.getDepositId());

        assertEquals(deposit.getDepositId(), gettedDeposit.getDepositId());
    }

    @Test
    void testGetDepositByIdWithException() {
        UUID randomUUID = UUID.randomUUID();
        try {
            Deposit gettedDeposit = depositService.getDepositById(deposit.getDepositId());
        }
        catch (Exception exception) {
            assertEquals("Deposit with id = " + randomUUID.toString() +" not found", exception.getMessage());
        }
    }

    @Test
    void testUpdateDepositById() throws Exception {
        DepositDTO depositDTO = new DepositDTO(20, 12, new Date(), client.getClientId(), bank.getBankId());
        depositDTO.setDepositDTOId(deposit.getDepositId());

        Deposit updatedDeposit = depositService.updateDeposit(depositDTO);

        assertEquals(depositDTO.getDepositId(), updatedDeposit.getDepositId());
        assertNotEquals(depositDTO.getPercent(), updatedDeposit.getPercent());
    }

    @Test
    void testUpdateDepositByIdWithException() {
        DepositDTO notExistDepositDTO = new DepositDTO(20, 12, new Date(), client.getClientId(), bank.getBankId());
        try {
            Deposit updatedDeposit = depositService.updateDeposit(notExistDepositDTO);
        }
        catch (Exception exception) {
            assertEquals("Deposit, client or bank not found", exception.getMessage());
        }
    }

    @Test
    void testDeleteDepositByIdWithException() {
        Deposit notExistDeposit = new Deposit(20, 12, new Date(), client, bank);
        try {
            depositService.deleteById(notExistDeposit.getDepositId());
        }
        catch (Exception exception) {
            assertEquals("Deposit with id = " + notExistDeposit.getDepositId().toString() +" not found", exception.getMessage());
        }
    }

    @Test
    void testGetDepositsReverse() {
        ArrayList<Deposit> depositList = new ArrayList<>();
        depositList.add(deposit3);
        depositList.add(deposit2);
        depositList.add(deposit1);
        depositList.add(deposit);
        depositList.sort(Collections.reverseOrder());
        FilterDTO filterDTO = new FilterDTO( new ArrayList<>(), true, 0, 10);

        List<Deposit> getDeposits = depositService.getDeposits(filterDTO);

        assertEquals(depositList.get(0).getDepositId(), getDeposits.get(0).getDepositId());
        assertEquals(depositList.get(1).getDepositId(), getDeposits.get(1).getDepositId());
        assertEquals(depositList.get(2).getDepositId(), getDeposits.get(2).getDepositId());
        assertEquals(depositList.get(3).getDepositId(), getDeposits.get(3).getDepositId());
    }

    @Test
    void testGetDepositsLimit() {
        ArrayList<Deposit> depositList = new ArrayList<>();
        depositList.add(deposit);
        depositList.add(deposit1);
        FilterDTO filterDTO = new FilterDTO( new ArrayList<>(), false, 0, 2);

        List<Deposit> getDeposits = depositService.getDeposits(filterDTO);

        assertEquals(depositList.get(0).getDepositId(), getDeposits.get(0).getDepositId());
        assertEquals(depositList.get(1).getDepositId(), getDeposits.get(1).getDepositId());
    }

    @Test
    void testGetDepositsOffset() {
        ArrayList<Deposit> depositList = new ArrayList<>();
        depositList.add(deposit1);
        depositList.add(deposit2);
        FilterDTO filterDTO = new FilterDTO( new ArrayList<>(), false, 1, 2);

        List<Deposit> getDeposits = depositService.getDeposits(filterDTO);

        assertEquals(depositList.get(0).getDepositId(), getDeposits.get(0).getDepositId());
        assertEquals(depositList.get(1).getDepositId(), getDeposits.get(1).getDepositId());
    }

    @Test
    void testGetDepositsFilter() {
        ArrayList<Deposit> depositList = new ArrayList<>();
        depositList.add(deposit1);
        ArrayList<FilterCondition> filterConditionList = new ArrayList<>();
        filterConditionList.add(new FilterCondition("percent", "10", Operator.EQUAL, ConcatenateType.OR));
        filterConditionList.add(new FilterCondition("periodInMonth", "12", Operator.NOT_EQUAL, ConcatenateType.AND));
        FilterDTO filterDTO = new FilterDTO(filterConditionList, false, 0, 3);

        List<Deposit> getDeposits = depositService.getDeposits(filterDTO);

        assertEquals(depositList.get(0).getDepositId(), getDeposits.get(0).getDepositId());
    }

    @Test
    void testGetDepositsWithFilterFieldNameException(){
        ArrayList<Deposit> depositList = new ArrayList<>();
        depositList.add(deposit1);
        depositList.add(deposit2);
        ArrayList<FilterCondition> filterConditionList = new ArrayList<>();
        filterConditionList.add(new FilterCondition("names", "clientName2", Operator.EQUAL, ConcatenateType.OR));
        filterConditionList.add(new FilterCondition("names", "clientName3", Operator.EQUAL, ConcatenateType.OR));
        FilterDTO filterDTO = new FilterDTO(filterConditionList, false, 0, 3);

        List<Deposit> getDeposits = depositService.getDeposits(filterDTO);

        assertEquals(0, getDeposits.size());
    }
}