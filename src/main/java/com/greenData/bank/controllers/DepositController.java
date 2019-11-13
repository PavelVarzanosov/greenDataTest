package com.greenData.bank.controllers;

import com.greenData.bank.models.Deposit;
import com.greenData.bank.modelsDTO.DepositDTO;
import com.greenData.bank.modelsDTO.FilterDTO;
import com.greenData.bank.services.depositService.IDepositService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.util.UUIDConverter;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/deposit")
public class DepositController {

    private final static Logger LOGGER = Logger.getLogger(BankController.class.getName());

    private IDepositService depositService;

    @Autowired
    public DepositController(@Qualifier("H2DepositImpl") IDepositService depositService) {
        this.depositService = depositService;
    }

    @PostMapping("/newDeposit")
    public ResponseEntity<Deposit> newDeposit(int percent,
                                              int dateInMonth,
                                              Long openingDate,
                                              @RequestParam(value="clientUUIDId") String clientUUIDId,
                                              @RequestParam(value="bankUUIDId") String  bankUUIDId  ) {
        try {
            UUID clientId = UUIDConverter.getUUID(clientUUIDId);
            UUID bankId = UUIDConverter.getUUID(bankUUIDId);
            Deposit deposit = depositService.saveDeposit(percent, dateInMonth, new Date(openingDate), clientId, bankId);
            return ResponseEntity.ok(deposit);
        } catch (NotFoundException e) {
            LOGGER.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Deposit> getDepositById(@PathVariable UUID id) {
        try {
            Deposit deposit = depositService.getDepositById(id);
            return ResponseEntity.ok(deposit);
        } catch (NotFoundException e) {
            LOGGER.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateDeposit")
    public ResponseEntity<Deposit> updateDeposit(@RequestBody DepositDTO depositDTO) {
        try {
            Deposit updatedDeposit = depositService.updateDeposit(depositDTO);
            return ResponseEntity.ok(updatedDeposit);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteDeposit(@PathVariable UUID id) {
        try {
            depositService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            LOGGER.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/getDeposits")
    public ResponseEntity<List<Deposit>> getDeposits(@RequestBody FilterDTO filterDTO) {
        List<Deposit> depositList = depositService.getDeposits(filterDTO);
        return ResponseEntity.ok(depositList);
    }
}