package com.greenData.bank.services.clientService.ClientServiceImpl;

import com.greenData.bank.models.Client;
import com.greenData.bank.models.enums.LegalForms;
import com.greenData.bank.modelsDTO.FilterCondition;
import com.greenData.bank.modelsDTO.FilterDTO;
import com.greenData.bank.services.clientService.IClientService;
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
class H2ClientServiceImplTest {

    @Autowired
    @Qualifier("H2ClientImpl")
    IClientService clientService;

    private Client client;
    private Client client1;
    private Client client2;
    private Client client3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        client = clientService.saveClient("clientName", "shortClientName", "address", LegalForms.IP);
        client1 = clientService.saveClient("clientName1", "shortClientName1", "address1", LegalForms.OOO);
        client2 = clientService.saveClient("clientName2", "shortClientName2", "address2", LegalForms.IP);
        client3 = clientService.saveClient("clientName3", "shortClientName3", "address3", LegalForms.IP);
    }

    @AfterEach
    void tearDown() {
        clientService.deleteAll();
    }

    @Test
    void testGetClientById() throws NotFoundException {

        Client gettedClient = clientService.getClientById(client.getClientId());

        assertEquals(client.getClientId(), gettedClient.getClientId());
    }

    @Test
    void testGetClientByIdWithException() {
        UUID randomUUID = UUID.randomUUID();
        try {
            Client gettedClient = clientService.getClientById(client.getClientId());
        }
        catch (Exception exception) {
            assertEquals("Client with id = " + randomUUID.toString() +" not found", exception.getMessage());
        }
    }

    @Test
    void testUpdateBankById() throws Exception {
        client.setName("noNameClient");

        Client updatedClient = clientService.updateClient(client);

        assertEquals(client.getClientId(), updatedClient.getClientId());
        assertNotEquals("bankName", updatedClient.getName());
    }

    @Test
    void testUpdateBankByIdWithException() {
        Client notExistClient = new Client("noNameClient", "shortClientName", "address", LegalForms.IP);
        try {
            Client updatedClient = clientService.updateClient(client);
        }
        catch (Exception exception) {
            assertEquals("Client with id = " + notExistClient.getClientId().toString() +" not found", exception.getMessage());
        }
    }

    @Test
    void testDeleteClientByIdWithException() {
        Client notExistClient = new Client("noNameClient", "shortClientName", "address", LegalForms.IP);
        try {
            clientService.deleteById(notExistClient.getClientId());
        }
        catch (Exception exception) {
            assertEquals("Client with id = " + notExistClient.getClientId().toString() +" not found", exception.getMessage());
        }
    }

    @Test
    void testGetClientsReverse() {
        ArrayList<Client> clientList = new ArrayList<>();
        clientList.add(client);
        clientList.add(client1);
        clientList.add(client2);
        clientList.add(client3);
        clientList.sort(Collections.reverseOrder());
        FilterDTO filterDTO = new FilterDTO( new ArrayList<>(), true, 0, 10);

        List<Client> getClients = clientService.getClients(filterDTO);

        assertEquals(clientList.get(0).getClientId(), getClients.get(0).getClientId());
        assertEquals(clientList.get(1).getClientId(), getClients.get(1).getClientId());
        assertEquals(clientList.get(2).getClientId(), getClients.get(2).getClientId());
        assertEquals(clientList.get(3).getClientId(), getClients.get(3).getClientId());
    }

    @Test
    void testGetClientsLimit() {
        ArrayList<Client> clientList = new ArrayList<>();
        clientList.add(client);
        clientList.add(client1);
        FilterDTO filterDTO = new FilterDTO( new ArrayList<>(), false, 0, 2);

        List<Client> getClients = clientService.getClients(filterDTO);

        assertEquals(clientList.get(0).getClientId(), getClients.get(0).getClientId());
        assertEquals(clientList.get(1).getClientId(), getClients.get(1).getClientId());
    }

    @Test
    void testGetClientsOffset() {
        ArrayList<Client> clientList = new ArrayList<>();
        clientList.add(client1);
        clientList.add(client2);
        FilterDTO filterDTO = new FilterDTO( new ArrayList<>(), false, 1, 2);

        List<Client> getClients = clientService.getClients(filterDTO);

        assertEquals(clientList.get(0).getClientId(), getClients.get(0).getClientId());
        assertEquals(clientList.get(1).getClientId(), getClients.get(1).getClientId());
    }

    @Test
    void testGetClientsFilter() {
        ArrayList<Client> clientList = new ArrayList<>();
        clientList.add(client1);
        clientList.add(client2);
        ArrayList<FilterCondition> filterConditionList = new ArrayList<>();
        filterConditionList.add(new FilterCondition("name", "clientName2", Operator.EQUAL, ConcatenateType.OR));
        filterConditionList.add(new FilterCondition("legalForm", "OOO", Operator.EQUAL, ConcatenateType.OR));
        FilterDTO filterDTO = new FilterDTO(filterConditionList, false, 0, 3);

        List<Client> getClients = clientService.getClients(filterDTO);

        assertEquals(clientList.get(0).getClientId(), getClients.get(0).getClientId());
        assertEquals(clientList.get(1).getClientId(), getClients.get(1).getClientId());
    }

    @Test
    void testGetClientsWithFilterFieldNameException(){
        ArrayList<Client> clientList = new ArrayList<>();
        clientList.add(client2);
        clientList.add(client3);
        ArrayList<FilterCondition> filterConditionList = new ArrayList<>();
        filterConditionList.add(new FilterCondition("names", "clientName2", Operator.EQUAL, ConcatenateType.OR));
        filterConditionList.add(new FilterCondition("names", "clientName3", Operator.EQUAL, ConcatenateType.OR));
        FilterDTO filterDTO = new FilterDTO(filterConditionList, false, 0, 3);

        List<Client> getClients = clientService.getClients(filterDTO);

        assertEquals(0, getClients.size());
    }
}