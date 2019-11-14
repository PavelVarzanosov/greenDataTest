package com.greenData.bank.controllers;

import com.greenData.bank.models.Bank;
import com.greenData.bank.modelsDTO.FilterDTO;
import com.greenData.bank.services.bankService.IBankService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/bank")
public class BankController {

    private final static Logger LOGGER = Logger.getLogger(BankController.class.getName());

    private IBankService bankService;

    @Autowired
    public BankController(@Qualifier("H2BankImpl") IBankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping("/newBank")
    public ResponseEntity<Bank> newBank(@RequestParam String name, Integer bik) {
        Bank bank = bankService.saveBank(name, bik);
        return ResponseEntity.ok(bank);
    }

    @GetMapping("{id}")
    public ResponseEntity<Bank> getBankById(@PathVariable  UUID id) {
        try {
            Bank bank = bankService.getBankById(id);
            return ResponseEntity.ok(bank);
        } catch (NotFoundException e) {
            LOGGER.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateBank")
    public ResponseEntity<Bank> updateBank(@RequestBody Bank bank) {
        try {
            Bank updatedBank = bankService.updateBank(bank);
            return ResponseEntity.ok(updatedBank);
        } catch (NotFoundException e) {
            LOGGER.info(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBank(@PathVariable UUID id) {
        try {
            bankService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            LOGGER.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/getBanks")
    public ResponseEntity<List<Bank>> getBanks(@RequestBody FilterDTO filterDTO) {
        List<Bank> bankList = bankService.getBanks(filterDTO);
        return ResponseEntity.ok(bankList);
    }
}