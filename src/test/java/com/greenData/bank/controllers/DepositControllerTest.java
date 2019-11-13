package com.greenData.bank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenData.bank.matchers.DepositDTOMatcher;
import com.greenData.bank.matchers.FilterDTOMatcher;
import com.greenData.bank.models.Bank;
import com.greenData.bank.models.Client;
import com.greenData.bank.models.Deposit;
import com.greenData.bank.models.enums.LegalForms;
import com.greenData.bank.modelsDTO.DepositDTO;
import com.greenData.bank.modelsDTO.FilterDTO;
import com.greenData.bank.services.depositService.IDepositService;
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
import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
class DepositControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IDepositService depositServiceMock;

    @InjectMocks
    private DepositController depositController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(depositController)
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void newDepositTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Date depositDate = new Date();
        Client client = new Client("clientName", "shortName", "address", LegalForms.IP);
        Bank bank = new Bank("bankName", 1);
        Deposit deposit = new Deposit(10, 10, depositDate, client, bank);
        doReturn(deposit).when(depositServiceMock)
                .saveDeposit(eq(10), eq(10), any(Date.class), any(UUID.class), any(UUID.class));

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/deposit/newDeposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .param("percent", "10")
                        .param("dateInMonth", "10")
                        .param("openingDate", Long.toString(depositDate.getTime()))
                        .param("clientUUIDId", client.getClientId().toString())
                        .param("bankUUIDId",  bank.getBankId().toString());
        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(deposit)));

        verify(depositServiceMock, times(1))
                .saveDeposit(eq(10), eq(10), eq(depositDate), eq(client.getClientId()), eq(bank.getBankId()));
        verifyNoMoreInteractions(depositServiceMock);
    }

    @Test
    void newDepositWithExceptionTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Date depositDate = new Date();
        Client client = new Client("clientName", "shortName", "address", LegalForms.IP);
        Bank bank = new Bank("bankName", 1);
        when(depositServiceMock
                .saveDeposit(eq(10), eq(10), eq(depositDate), any(UUID.class), any(UUID.class)))
                .thenThrow(new NotFoundException("Client or bank not found"));

        mockMvc.perform(
                post("/deposit/newDeposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .param("percent", "10")
                        .param("dateInMonth", "10")
                        .param("openingDate", Long.toString(depositDate.getTime()))
                        .param("clientUUIDId", client.getClientId().toString())
                        .param("bankUUIDId",  bank.getBankId().toString())
        )
                .andExpect(status().isNotFound());

        verify(depositServiceMock, times(1))
                .saveDeposit(eq(10), eq(10), eq(depositDate), eq(client.getClientId()), eq(bank.getBankId()));
        verifyNoMoreInteractions(depositServiceMock);
    }

    @Test
    void getDepositByIdTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Date depositDate = new Date();
        Client client = new Client("clientName", "shortName", "address", LegalForms.IP);
        Bank bank = new Bank("bankName", 1);
        Deposit deposit = new Deposit(10, 10, depositDate, client, bank);
        when(depositServiceMock.getDepositById(deposit.getDepositId())).thenReturn(deposit);

        mockMvc.perform(
                get("/deposit/{id}", deposit.getDepositId()))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(deposit)));

        verify(depositServiceMock, times(1)).getDepositById(deposit.getDepositId());
        verifyNoMoreInteractions(depositServiceMock);
    }

    @Test
    void getDepositByIdWithExceptionTest() throws Exception {
        UUID depositId = UUID.randomUUID();
        when(depositServiceMock.getDepositById(depositId)).thenThrow(new NotFoundException("Deposit with id = " + depositId.toString() +" not found"));

        mockMvc.perform(
                get("/deposit/{id}", depositId))
                .andExpect(status().isNotFound());

        verify(depositServiceMock, times(1)).getDepositById(depositId);
        verifyNoMoreInteractions(depositServiceMock);
    }

    @Test
    void updateDeposit() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Date depositDate = new Date();
        Client client = new Client("clientName", "shortName", "address", LegalForms.IP);
        Bank bank = new Bank("bankName", 1);
        DepositDTO updatedDepositDTO = new DepositDTO(10, 10, depositDate, client.getClientId(), bank.getBankId());
        Deposit updatedDeposit = new Deposit(10, 10, depositDate, client, bank);
        when(depositServiceMock.updateDeposit(any(DepositDTO.class))).thenReturn(updatedDeposit);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put("/deposit/updateDeposit")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsBytes(updatedDepositDTO));
        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(updatedDeposit)));

        verify(depositServiceMock, times(1)).updateDeposit(argThat(new DepositDTOMatcher(updatedDepositDTO)));
        verifyNoMoreInteractions(depositServiceMock);
    }

    @Test
    void updateDepositWithExceptionTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Date depositDate = new Date();
        Client client = new Client("clientName", "shortName", "address", LegalForms.IP);
        Bank bank = new Bank("bankName", 1);
        Deposit updatedDeposit = new Deposit(10, 10, depositDate, client, bank);
        when(depositServiceMock.updateDeposit(any(DepositDTO.class))).thenThrow(new NotFoundException("Deposit with id = " + updatedDeposit.getDepositId().toString() +" not found"));

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put("/deposit/updateDeposit")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsBytes(updatedDeposit));
        mockMvc.perform(builder)
                .andExpect(status().isNotFound());

        verify(depositServiceMock, times(1)).updateDeposit(any(DepositDTO.class));
        verifyNoMoreInteractions(depositServiceMock);
    }

    @Test
    void deleteDepositTest() throws Exception {
        UUID depositId = UUID.randomUUID();

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/deposit/{id}", depositId))
                .andExpect(status().isNoContent());

        verify(depositServiceMock, times(1)).deleteById(depositId);
        verifyNoMoreInteractions(depositServiceMock);
    }

    @Test
    void deleteDepositWithExceptionTest() throws Exception {
        UUID depositId = UUID.randomUUID();
        doThrow(new NotFoundException("Deposit with id = " + depositId.toString() +" not found")).when(depositServiceMock).deleteById(depositId);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/deposit/{id}", depositId))
                .andExpect(status().isNotFound());

        verify(depositServiceMock, times(1)).deleteById(depositId);
        verifyNoMoreInteractions(depositServiceMock);
    }

    @Test
    void getDepositReverseTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Bank bank = new Bank("name",1);
        Client client = new Client("name", "shortName", "address", LegalForms.IP);
        Deposit deposit = new Deposit(10, 12, new Date(), client, bank);
        Deposit deposit2 = new Deposit(15, 6, new Date(), client, bank);
        Deposit deposit3 = new Deposit(5, 3, new Date(), client, bank);
        ArrayList<Deposit> depositList = new ArrayList<>();
        depositList.add(deposit);
        depositList.add(deposit2);
        depositList.add(deposit3);
        Collections.reverse(depositList);
        FilterDTO filterDTO = new FilterDTO( new ArrayList<>(), true, 0, 10);
        when(depositServiceMock.getDeposits(any(FilterDTO.class))).thenReturn(depositList);

        mockMvc.perform(
                post("/deposit/getDeposits")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsBytes(filterDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(depositList)));

        verify(depositServiceMock, times(1)).getDeposits(argThat(new FilterDTOMatcher(filterDTO)));
        verifyNoMoreInteractions(depositServiceMock);
    }
}