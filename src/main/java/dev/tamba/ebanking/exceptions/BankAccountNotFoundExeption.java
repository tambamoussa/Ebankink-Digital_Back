package dev.tamba.ebanking.exceptions;

public class BankAccountNotFoundExeption extends Exception {
    public BankAccountNotFoundExeption(String message) {
        super(message);
    }
}
