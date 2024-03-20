package com.example.fraud_detection_service.service;

import com.example.fraud_detection_service.model.Transaction;
import com.example.fraud_detection_service.dao.TransactionRepository;
import com.example.fraud_detection_service.model.TransactionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FraudDetectionService {
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;

    @Autowired
    public FraudDetectionService(TransactionRepository transactionRepository, TransactionService transactionService) {
        this.transactionRepository = transactionRepository;
        this.transactionService = transactionService;
    }

    public boolean isFraudulentTransaction(Transaction transaction) {
        // Implement your fraud detection logic here
        return false;
    }

    public void detectFraud(TransactionEvent event) {
        // Check for transactions in more than 3 distinct services within a 5-minute window
        if (checkDistinctServicesWithinTimeWindow(event.getUserID(), event.getServiceID(), 5)) {
            System.out.println("Fraud detected for user with id" + event.getUserID() + ": transactions in more than 3 distinct services within 5 minutes.");
        }

        // Check for transactions 5x above the user's average transaction amount in the last 24 hours
        if (checkHighTransactionAmount(event.getUserID(), event.getAmount(), 5)) {
            System.out.println("Fraud detected for user " + event.getUserID() + ": transaction amount is 5x above the average in the last 24 hours.");
        }

        // Check for "ping-pong" activity within 10 minutes
        if (checkPingPongActivity(event.getUserID(), event.getServiceID(), event.getTimestamp(), 10)) {
            System.out.println("Fraud detected for user " + event.getUserID() + ": ping-pong activity detected within 10 minutes.");
        }
    }

    private boolean checkDistinctServicesWithinTimeWindow(String userId, String serviceId, int windowMinutes) {
        List<TransactionEvent> recentTransactions = transactionService.getRecentTransactions(userId, windowMinutes);
        Set<String> distinctServices = recentTransactions.stream().map(TransactionEvent::getServiceID).collect(Collectors.toSet());
        return distinctServices.size() > 3 && !distinctServices.contains(serviceId);
    }

    private boolean checkHighTransactionAmount(String userId, double amount, int multiplier) {
        double averageAmount = transactionService.calculateAverageTransactionAmount(userId, 24); // 24 hours
        return amount > (averageAmount * multiplier);
    }

    private boolean checkPingPongActivity(String userId, String serviceId, long timestamp, int windowMinutes) {
        List<TransactionEvent> recentTransactions = transactionService.getRecentTransactions(userId, windowMinutes);
        for (int i = 0; i < recentTransactions.size() - 1; i++) {
            TransactionEvent current = recentTransactions.get(i);
            TransactionEvent next = recentTransactions.get(i + 1);
            if (current.getServiceID().equals(serviceId) && next.getServiceID().equals(serviceId)) {
                long timeDifference = Math.abs(next.getTimestamp() - current.getTimestamp());
                if (timeDifference <= (windowMinutes * 60)) {
                    return true;
                }
            }
        }
        return false;
    }
}
