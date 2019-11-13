package com.greenData.bank.interfacesRepo;

import com.greenData.bank.models.Bank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IBank
        extends CrudRepository<Bank, UUID> {
}