package com.greenData.bank.services.clientService;

import com.greenData.bank.models.Client;
import com.greenData.bank.models.enums.LegalForms;
import com.greenData.bank.modelsDTO.FilterDTO;
import javassist.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface IClientService {

    Client saveClient(String name, String shortName, String address, LegalForms legalForm);

    Client getClientById(UUID id) throws NotFoundException;

    Client updateClient(Client client) throws Exception;

    void deleteById(UUID  id) throws NotFoundException;

    void deleteAll();

    List<Client> getClients(FilterDTO filterDTO);
}
