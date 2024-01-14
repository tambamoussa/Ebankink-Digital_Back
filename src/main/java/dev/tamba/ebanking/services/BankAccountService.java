package dev.tamba.ebanking.services;

import dev.tamba.ebanking.dtos.*;
import dev.tamba.ebanking.exceptions.BalanceNotSufficientException;
import dev.tamba.ebanking.exceptions.BankAccountNotFoundExeption;
import dev.tamba.ebanking.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CurrentBankAcountDTO saveCurrentBankAcount(double initialBalance, double overDraff, Long customerId) throws CustomerNotFoundException, CustomerNotFoundException;
    SavingBankAcountDTO saveSavingBankAcount(double initialBalance, double intersetRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundExeption;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundExeption, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BalanceNotSufficientException, BankAccountNotFoundExeption;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundExeption, BalanceNotSufficientException;

    List<BankAccountDTO> bankAcountList();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

   // List<AccountOperationDTO> accountHistory(String accountId);
}
