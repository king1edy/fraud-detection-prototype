package com.example.fraud_detection_service;

import com.example.fraud_detection_service.dao.TransactionRepository;
import com.example.fraud_detection_service.model.TransactionEvent;
import com.example.fraud_detection_service.service.TransactionBuffer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
public class FraudDetectionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FraudDetectionServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(TransactionBuffer transactionBuffer) {
        return args -> {
            // read json and write to db
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<TransactionEvent>> typeReference = new TypeReference<List<TransactionEvent>>(){};

            InputStream inputStream = TypeReference.class.getResourceAsStream("/data.json");

            try {
                List<TransactionEvent> transactionEvents = mapper.readValue(inputStream,typeReference);
                // transactionRepository.save(transactionEvents);
                for (TransactionEvent  event : transactionEvents){
                    transactionBuffer.addTransaction(event);
                }

                System.out.println(transactionEvents);
                System.out.println("Tr Saved!");
            } catch (IOException e){
                System.out.println("Unable to save users: " + e.getMessage());
            }
        };
    }
}