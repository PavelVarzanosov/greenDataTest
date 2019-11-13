package com.greenData.bank.services.bankService.bankServiceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.greenData.bank.models.Bank;
import com.greenData.bank.modelsDTO.FilterCondition;
import com.greenData.bank.modelsDTO.FilterDTO;
import com.greenData.bank.services.bankService.IBankService;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
class H2BankServiceImplTest {

    @Autowired
    @Qualifier("H2BankImpl")
    IBankService bankService;

    private Bank bank1;
    private Bank bank2;
    private Bank bank3;
    private Bank bank;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        bank1 = bankService.saveBank("name",1);
        bank2 = bankService.saveBank("name2",2);
        bank3 = bankService.saveBank("name3",3);
        bank = bankService.saveBank("bankName", 4);
    }

    @AfterEach
    void tearDown() {
        bankService.deleteAll();
    }

    @Test
    void testGetBankById() throws NotFoundException {

        Bank gettedBank = bankService.getBankById(bank.getBankId());

        assertEquals(bank.getBankId(), gettedBank.getBankId());
    }

    @Test
    void testGetBankByIdWithException() {
        UUID randomUUID = UUID.randomUUID();
        try {
            Bank gettedBank = bankService.getBankById(randomUUID);
        }
        catch (Exception exception) {
            assertEquals("Bank with id = " + randomUUID.toString() +" not found", exception.getMessage());
        }
    }

    @Test
    void testUpdateBankById() throws Exception {
        bank.setName("noNameBank");

        Bank updatedBank = bankService.updateBank(bank);

        assertEquals(bank.getBankId(), updatedBank.getBankId());
        assertNotEquals("bankName", updatedBank.getName());
    }

    @Test
    void testUpdateBankByIdWithException() {
        Bank notExistBank = new Bank("noNameBank", 111);
        try {
            Bank updatedBank = bankService.updateBank(notExistBank);
        }
        catch (Exception exception) {
            assertEquals("Bank with id = " + notExistBank.getBankId().toString() +" not found", exception.getMessage());
        }
    }

    @Test
    void testDeleteBankByIdWithException() {
        Bank notExistBank = new Bank("noNameBank", 111);
        try {
            bankService.deleteById(notExistBank.getBankId());
        }
        catch (Exception exception) {
            assertEquals("Bank with id = " + notExistBank.getBankId().toString() +" not found", exception.getMessage());
        }
    }

    @Test
    void testGetBanksReverse() throws JsonProcessingException {
        ArrayList<Bank> bankList = new ArrayList<>();
        bankList.add(bank);
        bankList.add(bank3);
        bankList.add(bank2);
        bankList.add(bank1);
        bankList.sort(Collections.reverseOrder());
        FilterDTO filterDTO = new FilterDTO( new ArrayList<>(), true, 0, 10);

        List<Bank> getBanks = bankService.getBanks(filterDTO);

        assertEquals(getBanks.get(0).getBankId(), bankList.get(0).getBankId());
        assertEquals(getBanks.get(1).getBankId(), bankList.get(1).getBankId());
        assertEquals(getBanks.get(2).getBankId(), bankList.get(2).getBankId());
        assertEquals(getBanks.get(3).getBankId(), bankList.get(3).getBankId());
    }

    @Test
    void testGetBanksLimit() throws JsonProcessingException {
        ArrayList<Bank> bankList = new ArrayList<>();
        bankList.add(bank1);
        bankList.add(bank2);
        FilterDTO filterDTO = new FilterDTO( new ArrayList<>(), false, 0, 2);

        List<Bank> getBanks = bankService.getBanks(filterDTO);

        assertEquals(getBanks.get(0).getBankId(), bankList.get(0).getBankId());
        assertEquals(getBanks.get(1).getBankId(), bankList.get(1).getBankId());
    }

    @Test
    void testGetBanksOffset() throws JsonProcessingException {
        ArrayList<Bank> bankList = new ArrayList<>();
        bankList.add(bank2);
        bankList.add(bank3);
        FilterDTO filterDTO = new FilterDTO( new ArrayList<>(), false, 1, 2);

        List<Bank> getBanks = bankService.getBanks(filterDTO);

        assertEquals(getBanks.get(0).getBankId(), bankList.get(0).getBankId());
        assertEquals(getBanks.get(1).getBankId(), bankList.get(1).getBankId());
    }

    @Test
    void testGetBanksFilter() throws JsonProcessingException {
        ArrayList<Bank> bankList = new ArrayList<>();
        bankList.add(bank2);
        bankList.add(bank3);
        ArrayList<FilterCondition> filterConditionList = new ArrayList<>();
        filterConditionList.add(new FilterCondition("name", "name2", Operator.EQUAL, ConcatenateType.OR));
        filterConditionList.add(new FilterCondition("name", "name3", Operator.EQUAL, ConcatenateType.OR));
        FilterDTO filterDTO = new FilterDTO(filterConditionList, false, 0, 3);

        List<Bank> getBanks = bankService.getBanks(filterDTO);

        assertEquals(getBanks.get(0).getBankId(), bankList.get(0).getBankId());
        assertEquals(getBanks.get(1).getBankId(), bankList.get(1).getBankId());
    }

    @Test
    void testGetBanksWithFilterFieldNameException() throws JsonProcessingException {
        ArrayList<Bank> bankList = new ArrayList<>();
        bankList.add(bank2);
        bankList.add(bank3);
        ArrayList<FilterCondition> filterConditionList = new ArrayList<>();
        filterConditionList.add(new FilterCondition("names", "name2", Operator.EQUAL, ConcatenateType.OR));
        filterConditionList.add(new FilterCondition("names", "name3", Operator.EQUAL, ConcatenateType.OR));
        FilterDTO filterDTO = new FilterDTO(filterConditionList, false, 0, 3);

        List<Bank> getBanks = bankService.getBanks(filterDTO);

        assertEquals(0, getBanks.size());
    }
}