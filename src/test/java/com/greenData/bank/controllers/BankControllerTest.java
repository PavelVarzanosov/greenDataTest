package com.greenData.bank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenData.bank.matchers.BankMatcher;
import com.greenData.bank.matchers.FilterDTOMatcher;
import com.greenData.bank.models.Bank;
import com.greenData.bank.modelsDTO.FilterDTO;
import com.greenData.bank.services.bankService.IBankService;
import javassist.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class BankControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IBankService bankServiceMock;

    @InjectMocks
    private BankController bankController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(bankController)
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testNewBank() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Bank bank = new Bank("bankName",1);
        doReturn(bank).when(bankServiceMock).saveBank("bankName",1);

        mockMvc.perform(
                post("/bank/newBank")
                        .param("name", "bankName")
                        .param("bik", "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(bank)));

        verify(bankServiceMock, times(1)).saveBank("bankName",1);
        verifyNoMoreInteractions(bankServiceMock);
    }

    @Test
    void testGetBankById() throws Exception {
        Bank bank = new Bank("name",1);
        when(bankServiceMock.getBankById(bank.getBankId())).thenReturn(bank);

        mockMvc.perform(
                get("/bank/{id}", bank.getBankId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bik").value("1"))
                .andExpect(jsonPath("$.name").value("name"));

        verify(bankServiceMock, times(1)).getBankById(bank.getBankId());
        verifyNoMoreInteractions(bankServiceMock);
    }

    @Test
    void testGetBankByIdWithException() throws Exception {
        UUID bankId = UUID.randomUUID();
        when(bankServiceMock.getBankById(bankId)).thenThrow(new NotFoundException("Bank with id = " + bankId.toString() +" not found"));

        mockMvc.perform(
                get("/bank/{id}", bankId))
                .andExpect(status().isNotFound());

        verify(bankServiceMock, times(1)).getBankById(bankId);
        verifyNoMoreInteractions(bankServiceMock);
    }

    @Test
    void testUpdateBank() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Bank updatedBank = new Bank("updateBankName",2);
        Bank oldBank = new Bank("oldBankName",3);
        when(bankServiceMock.updateBank(any(Bank.class))).thenReturn(updatedBank);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put("/bank/updateBank")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsBytes(updatedBank));
        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(updatedBank)));

        verify(bankServiceMock, times(1)).updateBank(argThat(new BankMatcher(updatedBank)));
        verifyNoMoreInteractions(bankServiceMock);
    }

    @Test
    void testUpdateBankWithException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Bank updatedBank = new Bank("updateBankName",2);
        when(bankServiceMock.updateBank(any(Bank.class))).thenThrow(new NotFoundException("Bank with id = " + updatedBank.getBankId().toString() +" not found"));

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put("/bank/updateBank")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsBytes(updatedBank));
        mockMvc.perform(builder)
                .andExpect(status().isNotFound());

        verify(bankServiceMock, times(1)).updateBank(any(Bank.class));
        verifyNoMoreInteractions(bankServiceMock);
    }

    @Test
    void testDeleteBank() throws Exception {
        UUID bankId = UUID.randomUUID();

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/bank/{id}", bankId))
                .andExpect(status().isNoContent());

        verify(bankServiceMock, times(1)).deleteById(bankId);
        verifyNoMoreInteractions(bankServiceMock);
    }

    @Test
    void testDeleteBankWithException() throws Exception {
        UUID bankId = UUID.randomUUID();
        doThrow(new NotFoundException("Bank with id = " + bankId.toString() +" not found")).when(bankServiceMock).deleteById(bankId);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/bank/{id}", bankId))
                .andExpect(status().isNotFound());

        verify(bankServiceMock, times(1)).deleteById(bankId);
        verifyNoMoreInteractions(bankServiceMock);
    }

    @Test
    void testGetBankReverseTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Bank bank = new Bank("name",1);
        Bank bank2 = new Bank("name2",2);
        Bank bank3 = new Bank("name3",3);
        ArrayList<Bank> bankReverseList = new ArrayList<>();
        bankReverseList.add(bank);
        bankReverseList.add(bank2);
        bankReverseList.add(bank3);
        Collections.reverse(bankReverseList);
        FilterDTO filterDTO = new FilterDTO( new ArrayList<>(), true, 0, 10);
                when(bankServiceMock.getBanks(any(FilterDTO.class))).thenReturn(bankReverseList);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/bank/getBanks")
                        .content(mapper.writeValueAsString(filterDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(bankReverseList)));

        verify(bankServiceMock, times(1)).getBanks(argThat(new FilterDTOMatcher(filterDTO)));
        verifyNoMoreInteractions(bankServiceMock);
    }

}