package dev.tamba.ebanking.dtos;

import dev.tamba.ebanking.Enums.AcountStatus;
import dev.tamba.ebanking.entities.Customer;
import lombok.Data;
import java.util.Date;



@Data
public class SavingBankAcountDTO extends BankAccountDTO{

    private  String id;
    private double balance;
    private Date createdAt;
    private AcountStatus status;
    private CustomerDTO customerDTO;
    private double interestRate;
}
