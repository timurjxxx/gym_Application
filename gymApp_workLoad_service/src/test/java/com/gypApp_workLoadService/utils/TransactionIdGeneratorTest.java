package com.gypApp_workLoadService.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionIdGeneratorTest {

    @Test
    public void testGenerateTransactionIdNotNull() {
        String transactionId = TransactionIdGenerator.generateTransactionId();
        assertNotNull(transactionId, "Generated transaction ID should not be null");
    }

    @Test
    public void testGenerateTransactionIdUnique() {
        String transactionId1 = TransactionIdGenerator.generateTransactionId();
        String transactionId2 = TransactionIdGenerator.generateTransactionId();

        assertNotEquals(transactionId1, transactionId2, "Generated transaction IDs should be unique");
    }

    @Test
    public void testGenerateTransactionIdLength() {
        String transactionId = TransactionIdGenerator.generateTransactionId();
        assertEquals(36, transactionId.length(), "Generated transaction ID should have length 36");
    }

    @Test
    public void testTransactionIdGeneratorNotNull() {
        TransactionIdGenerator idGenerator = new TransactionIdGenerator();
        assertNotNull(idGenerator, "TransactionIdGenerator instance should not be null");
    }
}