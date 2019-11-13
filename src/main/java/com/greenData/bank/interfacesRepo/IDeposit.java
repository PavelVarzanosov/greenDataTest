package com.greenData.bank.interfacesRepo;

import com.greenData.bank.models.Deposit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IDeposit
        extends CrudRepository<Deposit, UUID> {
}