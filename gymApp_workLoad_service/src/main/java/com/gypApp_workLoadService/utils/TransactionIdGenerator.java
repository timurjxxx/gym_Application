package com.gypApp_workLoadService.utils;

import java.util.UUID;

public class TransactionIdGenerator {

    public static String generateTransactionId() {
        return UUID.randomUUID().toString();
    }
}