package com.greenData.bank.services.clientService.ClientServiceImpl;

import com.greenData.bank.interfacesRepo.IClient;
import com.greenData.bank.models.Client;
import com.greenData.bank.models.enums.LegalForms;
import com.greenData.bank.modelsDTO.FilterCondition;
import com.greenData.bank.modelsDTO.FilterDTO;
import com.greenData.bank.services.clientService.IClientService;
import com.greenData.bank.specification.abstractSpecification.Specification;
import com.greenData.bank.specification.abstractSpecification.SpecificationAbstractImpl;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Component("H2ClientImpl")
public class H2ClientServiceImpl implements IClientService {

    @Autowired
    private IClient clientRep;

    public Client saveClient(String name, String shortName, String address, LegalForms legalForm) {
        Client client = new Client(name, shortName, address, legalForm);

        this.clientRep.save(client);
        return client;
    }

    public Client getClientById(UUID id) throws NotFoundException {
        Optional<Client> client = this.clientRep.findById(id);

        if(client.isPresent()){
            return client.get();
        }
        else {
            throw new NotFoundException("Client with id = " + id.toString() +" not found");
        }
    }

    public Client updateClient(Client client) throws NotFoundException {
        final Object lock = new Object();
        Optional<Client> optionalClient = this.clientRep.findById(client.getClientId());

        synchronized(lock) {
            if (optionalClient.isPresent()) {
                Client savedClient = this.clientRep.save(client);
                return savedClient;
            }
            else {
                throw new NotFoundException("Client with id = " + client.getClientId().toString() +" not found");
            }
        }
    }

    public void deleteById(UUID  id) throws NotFoundException {
        if (this.clientRep.findById(id).isPresent()) {
            clientRep.deleteById(id);
        }
        else {
            throw new NotFoundException("Client with id = " + id.toString() +" not found");
        }
    }

    public void deleteAll() {
        clientRep.deleteAll();
    }

    public List<Client> getClients(FilterDTO filterDTO) {
        Iterable<Client> iterableClient = clientRep.findAll();
        List<Client> clientList;
        List<Client> finalClientList = new ArrayList<>();

        if(filterDTO.getFilterConditionList().size() > 0) {
            Specification<Client> finalCommonSpecification = getCommonSpecification(filterDTO);
            iterableClient.forEach((client) -> {
                try {
                    if(client.satisfies(finalCommonSpecification)) finalClientList.add(client);
                } catch (Exception e) {
                    //TODO прокинуть exception выше
                }
            });
        } else {
            iterableClient.forEach((client) -> {
                finalClientList.add(client);
            });
        }
        Comparator<Client> clientComparator = Comparator.<Client> naturalOrder();
        clientList = finalClientList.stream()
                .sorted(clientComparator)
                .skip(filterDTO.getOffset())
                .limit(filterDTO.getLimit())
                .collect(Collectors.toList());
        if (filterDTO.getIsReverse()) Collections.sort(clientList, Collections.reverseOrder());
        return clientList;
    }

    private Specification<Client> getCommonSpecification(FilterDTO filterDTO) {
        FilterCondition filter = filterDTO.getFilterConditionList().get(0);
        Specification<Client> commonSpecification = new SpecificationAbstractImpl<Client>(filter.getFieldName(), filter.getOperator(), filter.getValueCondition());

        for (int i = 1; i< filterDTO.getFilterConditionList().size(); i++) {
            filter = filterDTO.getFilterConditionList().get(i);
            commonSpecification = commonSpecification.concatenate(filter.getConcatenateType(),
                    new SpecificationAbstractImpl<Client>(filter.getFieldName(), filter.getOperator(), filter.getValueCondition()));
        }

        return commonSpecification;
    }
}
