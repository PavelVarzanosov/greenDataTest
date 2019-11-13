package com.greenData.bank.models;

import com.greenData.bank.models.enums.LegalForms;
import com.greenData.bank.specification.abstractSpecification.Specification;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "Client")
public class Client implements Comparable{
    @Id
    @Column(name = "ID", nullable = false)
    private UUID clientId;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "SHORT_NAME", nullable = false)
    private String shortName;
    @Column(name = "ADDRESS", length = 64, nullable = false)
    private String address;
    @Column(name = "LEGAL_FORM")
    @Enumerated(EnumType.STRING)
    private LegalForms legalForm;

    public Client (){

    }

    public Client (String name, String shortName, String address, LegalForms legalForm){
        this.name = name;
        this.shortName = shortName;
        this.address = address;
        this.legalForm = legalForm;
        this.clientId = UUID.randomUUID();
    }

    public UUID getClientId() {
        return this.clientId;
    }

    public String getName() {
        return this.name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public String getAddress() {
        return this.address;
    }

    public LegalForms getLegalForm() {
        return this.legalForm;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean satisfies(Specification specification) throws Exception {
        return specification.isSatisfiedBy(this);
    }

    @Override
    public int compareTo(Object o) {
        Client client = (Client)o;
        return this.getName().compareTo(client.getName());
    }
}
