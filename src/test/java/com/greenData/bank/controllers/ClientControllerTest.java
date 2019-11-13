package com.greenData.bank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenData.bank.matchers.ClientMatcher;
import com.greenData.bank.matchers.FilterDTOMatcher;
import com.greenData.bank.models.Client;
import com.greenData.bank.models.enums.LegalForms;
import com.greenData.bank.modelsDTO.FilterDTO;
import com.greenData.bank.services.clientService.IClientService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IClientService clientServiceMock;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(clientController)
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testNewClient() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Client client = new Client("clientName", "shortName", "address", LegalForms.IP);
        doReturn(client).when(clientServiceMock).saveClient("clientName", "shortName", "address", LegalForms.IP);

        mockMvc.perform(
                post("/client/newClient")
                        .param("name", "clientName")
                        .param("shortName", "shortName")
                        .param("address","address")
                        .param("legalForm", LegalForms.IP.toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(client)));

        verify(clientServiceMock, times(1)).saveClient("clientName", "shortName", "address", LegalForms.IP);
        verifyNoMoreInteractions(clientServiceMock);
    }

    @Test
    void testGetClientById() throws Exception {
        Client client = new Client("clientName", "shortName", "address", LegalForms.IP);
        when(clientServiceMock.getClientById(client.getClientId())).thenReturn(client);

        mockMvc.perform(
                get("/client/{id}", client.getClientId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("clientName"))
                .andExpect(jsonPath("$.shortName").value("shortName"))
                .andExpect(jsonPath("$.address").value("address"))
                .andExpect(jsonPath("$.legalForm").value(LegalForms.IP.toString()));

        verify(clientServiceMock, times(1)).getClientById(client.getClientId());
        verifyNoMoreInteractions(clientServiceMock);
    }

    @Test
    void testGetClientByIdWithException() throws Exception {
        UUID clientId = UUID.randomUUID();
        when(clientServiceMock.getClientById(clientId)).thenThrow(new NotFoundException("Client with id = " + clientId.toString() +" not found"));

        mockMvc.perform(
                get("/client/{id}", clientId))
                .andExpect(status().isNotFound());

        verify(clientServiceMock, times(1)).getClientById(clientId);
        verifyNoMoreInteractions(clientServiceMock);
    }

    @Test
    void testUpdateClient() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Client updatedClient = new Client("clientName", "shortName", "address", LegalForms.IP);
        when(clientServiceMock.updateClient(any(Client.class))).thenReturn(updatedClient);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put("/client/updateClient")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsBytes(updatedClient));
        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(updatedClient)));

        verify(clientServiceMock, times(1)).updateClient(argThat(new ClientMatcher(updatedClient)));
        verifyNoMoreInteractions(clientServiceMock);
    }

    @Test
    void testUpdateClientWithException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Client updatedClient = new Client("clientName", "shortName", "address", LegalForms.IP);
        when(clientServiceMock.updateClient(any(Client.class))).thenThrow(new NotFoundException("Client with id = " + updatedClient.getClientId().toString() +" not found"));

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put("/client/updateClient")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsBytes(updatedClient));
        mockMvc.perform(builder)
                .andExpect(status().isNotFound());

        verify(clientServiceMock, times(1)).updateClient(any(Client.class));
        verifyNoMoreInteractions(clientServiceMock);
    }

    @Test
    void testDeleteClient() throws Exception {
        UUID clientId = UUID.randomUUID();

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/client/{id}",clientId))
                .andExpect(status().isNoContent());

        verify(clientServiceMock, times(1)).deleteById(clientId);
        verifyNoMoreInteractions(clientServiceMock);
    }

    @Test
    void testDeleteClientWithException() throws Exception {
        UUID clientId = UUID.randomUUID();
        doThrow(new NotFoundException("Client with id = " + clientId.toString() +" not found")).when(clientServiceMock).deleteById(clientId);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/client/{id}", clientId))
                .andExpect(status().isNotFound());

        verify(clientServiceMock, times(1)).deleteById(clientId);
        verifyNoMoreInteractions(clientServiceMock);
    }

    @Test
    void testGetClientReverse() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Client client = new Client("name", "shortName", "address", LegalForms.IP);
        Client client2 = new Client("name2", "shortName2", "address2", LegalForms.OOO);
        Client client3 = new Client("name3", "shortName3", "address3", LegalForms.IP);
        ArrayList<Client> clientList = new ArrayList<>();
        clientList.add(client);
        clientList.add(client2);
        clientList.add(client3);
        Collections.reverse(clientList);
        FilterDTO filterDTO = new FilterDTO( new ArrayList<>(), true, 0, 10);
        when(clientServiceMock.getClients(any(FilterDTO.class))).thenReturn(clientList);

        mockMvc.perform(
                post("/client/getClients")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsBytes(filterDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(clientList)));

        verify(clientServiceMock, times(1)).getClients(argThat(new FilterDTOMatcher(filterDTO)));
        verifyNoMoreInteractions(clientServiceMock);
    }

}