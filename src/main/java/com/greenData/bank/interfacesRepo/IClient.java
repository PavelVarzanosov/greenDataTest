package com.greenData.bank.interfacesRepo;

import com.greenData.bank.models.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IClient
        extends CrudRepository<Client, UUID> {
}