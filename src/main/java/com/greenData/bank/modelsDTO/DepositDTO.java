package com.greenData.bank.modelsDTO;

import java.util.Date;
import java.util.UUID;

public class DepositDTO {

    private UUID depositId;
    private int percent;
    private int dateInMonth;
    private Date openingDate;
    private UUID clientId;
    private UUID bankId;

    public DepositDTO () {

    }

    public DepositDTO (int percent, int dateInMonth, Date openingDate, UUID clientId, UUID bankId){
        this.percent = percent;
        this.dateInMonth = dateInMonth;
        this.openingDate = openingDate;
        this.clientId = clientId;
        this.bankId = bankId;
        this.depositId=UUID.randomUUID();
    }

    public UUID getDepositId() {
        return this.depositId;
    }

    public int getPercent() {
        return this.percent;
    }

    public int getDateInMonth() {
        return this.dateInMonth;
    }

    public Date getOpeningDate() {
        return this.openingDate;
    }

    public UUID getClientId() {
        return this.clientId;
    }

    public UUID getBankId() {
        return this.bankId;
    }

    public void setDepositDTOId(UUID depositId) {
        this.depositId = depositId;
    }
}
