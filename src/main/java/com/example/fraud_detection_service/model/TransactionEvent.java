package com.example.fraud_detection_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionEvent {
    long timestamp;
    double amount;
    String userID;
    String serviceID;

    public TransactionEvent(){}

//    public TransactionEvent(long timestamp, double amount, String userID, String serviceID) {
//        this.timestamp = timestamp;
//        this.amount = amount;
//        this.userID = userID;
//        this.serviceID = serviceID;
//    }
//
//    public long getTimestamp() {
//        return timestamp;
//    }
//
//    public double getAmount() {
//        return amount;
//    }
//
//    public String getUserID() {
//        return userID;
//    }
//
//    public String getServiceID() {
//        return serviceID;
//    }
}

