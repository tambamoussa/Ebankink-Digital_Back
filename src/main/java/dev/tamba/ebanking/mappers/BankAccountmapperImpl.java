package dev.tamba.ebanking.mappers;

import dev.tamba.ebanking.dtos.AccountOperationDTO;
import dev.tamba.ebanking.dtos.CurrentBankAcountDTO;
import dev.tamba.ebanking.dtos.CustomerDTO;
import dev.tamba.ebanking.dtos.SavingBankAcountDTO;
import dev.tamba.ebanking.entities.AccountOperation;
import dev.tamba.ebanking.entities.CurrentAcount;
import dev.tamba.ebanking.entities.Customer;
import dev.tamba.ebanking.entities.SavingAcount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountmapperImpl {
    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
        return customerDTO;
    }
    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return customer;
    }

    public SavingBankAcountDTO fromSavingBankAccount(SavingAcount savingAcount){
        SavingBankAcountDTO savingBankAcountDTO=new SavingBankAcountDTO();
        BeanUtils.copyProperties(savingAcount,savingBankAcountDTO);
        savingBankAcountDTO.setCustomerDTO(fromCustomer(savingAcount.getCustomer()));
        savingBankAcountDTO.setType(savingAcount.getClass().getSimpleName());
        return savingBankAcountDTO;
        }


    public SavingAcount fromSavingBankAccountDTO(SavingBankAcountDTO savingBankAcountDTO){
            SavingAcount savingAcount=new SavingAcount();
            savingAcount.setCustomer(fromCustomerDTO(savingBankAcountDTO.getCustomerDTO()));
            return savingAcount;
    }

    public CurrentBankAcountDTO fromCurrentBankAccount(CurrentAcount currentAcount){
           CurrentBankAcountDTO currentBankAcountDTO=new CurrentBankAcountDTO();
           BeanUtils.copyProperties(currentAcount,currentBankAcountDTO);
           currentBankAcountDTO.setCustomerDTO(fromCustomer(currentAcount.getCustomer()));
        currentBankAcountDTO.setType(currentAcount.getClass().getSimpleName());
           return currentBankAcountDTO;
    }

    public CurrentAcount fromCurrentBankAccountDTO(CurrentBankAcountDTO currentBankAcountDTO){
           CurrentAcount currentAcount=new CurrentAcount();
           BeanUtils.copyProperties(currentBankAcountDTO,currentAcount);
           currentAcount.setCustomer(fromCustomerDTO(currentBankAcountDTO.getCustomerDTO()));
           return currentAcount;
    }

   /*public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation){
        AccountOperationDTO accountOperationDTO=new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation, accountOperationDTO);
        return accountOperationDTO;
    }*/
}
