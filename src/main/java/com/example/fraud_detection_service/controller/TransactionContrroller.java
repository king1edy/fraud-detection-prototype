package com.example.fraud_detection_service.controller;

import com.example.fraud_detection_service.model.Transaction;
import com.example.fraud_detection_service.service.FraudDetectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionContrroller {
    private final FraudDetectionService fraudDetectionService;

    public TransactionContrroller(FraudDetectionService fraudDetectionService) {
        this.fraudDetectionService = fraudDetectionService;
    }

    @PostMapping("/transactions")
    public ResponseEntity<?> handleTransaction(@RequestBody Transaction transaction) {
        boolean isFraudulent = fraudDetectionService.isFraudulentTransaction(transaction);
        if (isFraudulent) {
            // Handle fraudulent transaction
        }
        return ResponseEntity.ok().build();
    }
}
