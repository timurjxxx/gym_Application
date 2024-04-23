package com.gypApp_main.utils;


import java.util.UUID;

public class TransactionIdGenerator {

    public static String generateTransactionId() {
        return UUID.randomUUID().toString();
    }
}