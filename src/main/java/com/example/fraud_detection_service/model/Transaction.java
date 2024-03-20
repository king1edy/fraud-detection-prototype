package com.example.fraud_detection_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    long timestamp;
    double amount;
    String userId;
    String serviceId;

    public Transaction(long timestamp, double amount, String userId, String serviceId) {
        this.timestamp = timestamp;
        this.amount = amount;
        this.userId = userId;
        this.serviceId = serviceId;
    }

    public Transaction() { }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserID(String userId) {
        this.userId = userId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceID(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "timestamp=" + timestamp +
                ", amount=" + amount +
                ", userId='" + userId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                '}';
    }
}

