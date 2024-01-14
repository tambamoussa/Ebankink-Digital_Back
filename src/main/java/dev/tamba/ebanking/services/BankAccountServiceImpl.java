package dev.tamba.ebanking.services;

import dev.tamba.ebanking.Enums.OperationType;
import dev.tamba.ebanking.dtos.*;
import dev.tamba.ebanking.entities.*;
import dev.tamba.ebanking.exceptions.BalanceNotSufficientException;
import dev.tamba.ebanking.exceptions.BankAccountNotFoundExeption;
import dev.tamba.ebanking.exceptions.CustomerNotFoundException;
import dev.tamba.ebanking.mappers.BankAccountmapperImpl;
import dev.tamba.ebanking.repositories.AccountOperationRepository;
import dev.tamba.ebanking.repositories.BankAccountRepository;
import dev.tamba.ebanking.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService{

    BankAccountRepository bankAccountRepository;
    CustomerRepository customerRepository;
    AccountOperationRepository accountOperationRepository;
    private BankAccountmapperImpl dtoMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer= customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAcountDTO saveCurrentBankAcount(double initialBalance, double overDraff, Long customerId) throws CustomerNotFoundException, CustomerNotFoundException {
       Customer customer = customerRepository.findById(customerId).orElse(null);
       if (customer==null)
           throw new CustomerNotFoundException("Customer not found");
       CurrentAcount currentAcount = new CurrentAcount();
       currentAcount.setId(UUID.randomUUID().toString());
       currentAcount.setCreatedAt(new Date());
       currentAcount.setBalance(initialBalance);
       currentAcount.setOverDraft(overDraff);
       currentAcount.setCustomer(customer);
        CurrentAcount savedBankAccount = bankAccountRepository.save(currentAcount);
        return dtoMapper.fromCurrentBankAccount(savedBankAccount);
    }

    @Override
    public SavingBankAcountDTO saveSavingBankAcount(double initialBalance, double intersetRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer==null)
            throw new CustomerNotFoundException("Customer not found");
        SavingAcount savingAcount = new SavingAcount();
        savingAcount.setId(UUID.randomUUID().toString());
        savingAcount.setCreatedAt(new Date());
        savingAcount.setBalance(initialBalance);
        savingAcount.setInterestRate(intersetRate);
        savingAcount.setCustomer(customer);
        SavingAcount savedBankAccount = bankAccountRepository.save(savingAcount);
        return dtoMapper.fromSavingBankAccount(savedBankAccount);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream()
                .map(cust -> dtoMapper
                .fromCustomer(cust))
                .collect(Collectors.toList());
       /* List<CustomerDTO> customerDTOS = new ArrayList<>();
        for (Customer customer:customers){
            CustomerDTO customerDTO=dtoMapper.fromCustomer(customer);
            customerDTOS.add(customerDTO);
        }*/
        return customerDTOS;

    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundExeption {
        BankAcount bankAcount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundExeption("Bank account not found"));
        if (bankAcount instanceof SavingAcount){
            SavingAcount savingAcount=(SavingAcount) bankAcount;
            return dtoMapper.fromSavingBankAccount(savingAcount);
        } else {
            CurrentAcount currentAcount= (CurrentAcount) bankAcount;
            return dtoMapper.fromCurrentBankAccount(currentAcount);
        }

    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundExeption, BalanceNotSufficientException {
        BankAcount bankAcount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundExeption("Bank account not found"));
        if (bankAcount.getBalance()<amount)
            throw new BalanceNotSufficientException("Balance not sufficient");
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAcount(bankAcount);
        accountOperationRepository.save(accountOperation);
        bankAcount.setBalance(bankAcount.getBalance()-amount);
        bankAccountRepository.save(bankAcount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BalanceNotSufficientException, BankAccountNotFoundExeption {
        BankAcount bankAcount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundExeption("Bank account not found"));
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAcount(bankAcount);
        accountOperationRepository.save(accountOperation);
        bankAcount.setBalance(bankAcount.getBalance()+amount);
        bankAccountRepository.save(bankAcount);

    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundExeption, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer to"+accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from"+accountIdSource);
    }
    @Override
    public List<BankAccountDTO> bankAcountList(){
        List<BankAcount> bankAcounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAcounts.stream().map(bankAcount -> {
            if (bankAcount instanceof SavingAcount) {
                SavingAcount savingAcount = (SavingAcount) bankAcount;
                return dtoMapper.fromSavingBankAccount(savingAcount);
            } else {
                CurrentAcount currentAcount = (CurrentAcount) bankAcount;
                return dtoMapper.fromCurrentBankAccount(currentAcount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;

    }
    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return dtoMapper.fromCustomer(customer);
    }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer= customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }
    @Override
    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }
  /*  @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
       return accountOperations.stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
    }*/

}
