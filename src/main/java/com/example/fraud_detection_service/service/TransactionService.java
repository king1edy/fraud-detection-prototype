package com.example.fraud_detection_service.service;

import com.example.fraud_detection_service.model.TransactionEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final Map<String, List<TransactionEvent>> userTransactions = new ConcurrentHashMap<>();

    // Call this method to add a new transaction
    public void addTransaction(TransactionEvent event) {
        userTransactions.computeIfAbsent(event.getUserID(), k -> new ArrayList<>()).add(event);
        userTransactions.get(event.getUserID()).sort(Comparator.comparingLong(TransactionEvent::getTimestamp));
    }

//    public Map<String, List<TransactionEvent>> getTransactions(){
//        return userTransactions;
//    }
    // Gets recent transactions within a specified time window (in minutes)
    public List<TransactionEvent> getRecentTransactions(String userId, int windowMinutes) {
        long currentTime = System.currentTimeMillis() / 1000L; // Current time in seconds
        long windowStart = currentTime - (windowMinutes * 60); // Calculate window start time

        return userTransactions.getOrDefault(userId, new ArrayList<>())
                .stream()
                .filter(event -> event.getTimestamp() >= windowStart)
                .collect(Collectors.toList());
    }

    // Calculates the average transaction amount over the last 24 hours
    public double calculateAverageTransactionAmount(String userId, int hours) {
        long currentTime = System.currentTimeMillis() / 1000L; // Current time in seconds
        long windowStart = currentTime - (hours * 60 * 60); // Calculate window start time

        List<TransactionEvent> recentTransactions = userTransactions.getOrDefault(userId, new ArrayList<>())
                .stream()
                .filter(event -> event.getTimestamp() >= windowStart)
                .collect(Collectors.toList());

        if (recentTransactions.isEmpty()) {
            return 0.0;
        }

        double totalAmount = recentTransactions.stream()
                .mapToDouble(TransactionEvent::getAmount)
                .sum();

        return totalAmount / recentTransactions.size();
    }
}
