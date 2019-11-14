package com.greenData.bank.models;

import com.greenData.bank.specification.abstractSpecification.Specification;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "Deposit")
public class Deposit implements Comparable{
    @Id
    @Column(name = "ID", nullable = false)
    private UUID depositId;
    @Column(name = "PERCENT", nullable = false)
    private int percent;
    @Column(name = "DATE_IN_MONTH", nullable = false)
    private int periodInMonth;
    @Column(name = "OPENING_DATE", nullable = false)
    private Date openingDate;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "CLIENT_ID", nullable = false)
    private Client client;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "BANK_ID", nullable = false)
    private Bank bank;

    public Deposit () {

    }

    public Deposit (int percent, int periodInMonth, Date openingDate, Client client, Bank bank){
        this.percent = percent;
        this.periodInMonth = periodInMonth;
        this.openingDate = openingDate;
        this.client = client;
        this.bank = bank;
        this.depositId=UUID.randomUUID();
    }

    public UUID getDepositId() {
        return this.depositId;
    }

    public int getPercent() {
        return this.percent;
    }

    public Date getOpeningDate() {
        return this.openingDate;
    }

    public Client getClient() {
        return this.client;
    }

    public Bank getBank() {
        return this.bank;
    }

    public boolean satisfies(Specification specification) throws Exception {
        return specification.isSatisfiedBy(this);
    }

    @Override
    public int compareTo(Object o) {
        Deposit deposit = (Deposit)o;
        return this.getPercent() != deposit.getPercent()
                ? Integer.compare(this.getPercent(), deposit.getPercent())
                : Long.compare( this.getOpeningDate().getTime(), deposit.getOpeningDate().getTime());
    }
}
