package com.greenData.bank.models;

import com.greenData.bank.specification.abstractSpecification.Specification;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "Bank")
public class Bank implements Comparable{
    @Id
    @Column(name = "ID", nullable = false)
    private UUID  bankId;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "BIK", nullable = false)
    private int bik;

    public Bank (){

    }

    public Bank (String name, int bik){
        this.name = name;
        this.bik = bik;
        this.bankId = UUID.randomUUID();
    }

    public UUID getBankId() {
        return this.bankId;
    }

    public String getName() {
        return this.name;
    }

    public int getBik() {
        return this.bik;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean satisfies(Specification specification) throws Exception {
        return specification.isSatisfiedBy(this);
    }

    @Override
    public int compareTo(Object o) {
        Bank bank = (Bank)o;
        return Integer.compare(this.getBik(), bank.getBik());
    }
}
