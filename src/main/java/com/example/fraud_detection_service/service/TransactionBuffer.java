package com.example.fraud_detection_service.service;

import com.example.fraud_detection_service.model.TransactionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.Executors;

@Component
public class TransactionBuffer {

    private final PriorityQueue<TransactionEvent> queue = new PriorityQueue<>(Comparator.comparingLong(TransactionEvent::getTimestamp));
    private final long bufferDelayInSeconds;
    private final FraudDetectionService fraudDetectionService;
    private final TransactionService transactionService;

    @Autowired
    public TransactionBuffer(@Value("${buffer.delay.seconds:30}") long bufferDelayInSeconds, FraudDetectionService fraudDetectionService, TransactionService transactionService) {
        this.bufferDelayInSeconds = bufferDelayInSeconds;
        this.fraudDetectionService = fraudDetectionService;
        this.transactionService = transactionService;
        startBufferProcessor();
    }

    public synchronized void addTransaction(TransactionEvent event) {
        queue.offer(event);
        transactionService.addTransaction(event);
    }

    private void startBufferProcessor() {
        Executors.newSingleThreadExecutor().submit(() -> {
            while (true) {
                processBuffer();
                try {
                    Thread.sleep(1000); // Sleep for 1 second before checking the buffer again
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    private synchronized void processBuffer() {
        long now = System.currentTimeMillis() / 1000L;
        while (!queue.isEmpty() && (now - queue.peek().getTimestamp() >= bufferDelayInSeconds)) {
            TransactionEvent event = queue.poll();
            processTransaction(event);
        }
    }

    private void processTransaction(TransactionEvent event) {
        // Process the transaction, e.g., check for fraud here...
        fraudDetectionService.detectFraud(event);

        // print transactions here...
        // System.out.println(transactionService.getTransactions());
    }
}

